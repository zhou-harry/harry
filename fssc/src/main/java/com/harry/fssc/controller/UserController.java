package com.harry.fssc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.harry.fssc.aop.logger.BehaviorManage;
import com.harry.fssc.aop.logger.LoggerManage;
import com.harry.fssc.enums.AspectType;
import com.harry.fssc.model.Role;
import com.harry.fssc.model.RoleUser;
import com.harry.fssc.model.User;
import com.harry.fssc.result.ResponseData;
import com.harry.fssc.result.ResultCode;
import com.harry.fssc.service.AttachmentService;
import com.harry.fssc.service.UserService;
import com.harry.fssc.session.SessionUtil;
import com.harry.fssc.util.Const;
import com.harry.fssc.util.PasswordHelper;

@RestController
@RequestMapping("user")
public class UserController {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private SessionUtil sessionUtil;

	/**
	 * 获取用户登录信息
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/userInfo", method = RequestMethod.POST)
	public ResponseData getUserInfo(HttpServletRequest request, HttpServletResponse response) {
		User userInfo = sessionUtil.getUserInfo(request, response);
		if (null != userInfo) {
			return new ResponseData(ResultCode.SUCCESS, userInfo.response());
		}
		return new ResponseData(ResultCode.FAILED, null);
	}
	
	@ResponseBody
	@RequestMapping(value = "/userList", method = RequestMethod.POST)
	public ResponseData findUsers(HttpServletRequest request, HttpServletResponse response) {
		List<User> users = userService.findAll();
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			user.setPassword(null);
			user.setSalt(null);
		}
		return new ResponseData(ResultCode.SUCCESS, users);
	}
	
	@ResponseBody
	@RequestMapping(value = "/info", method = RequestMethod.POST)
	public ResponseData findUserByid(HttpServletRequest request, HttpServletResponse response,String userid) {
		User u = userService.findUserById(userid);
		if (null!=u) {
			u.setPassword(null);
			u.setSalt(null);
		}
		return new ResponseData(ResultCode.SUCCESS, u);
	}
	
	@ResponseBody
	@RequestMapping(value = "/userActive", method = RequestMethod.POST)
	public ResponseData userActive(HttpServletRequest request, HttpServletResponse response) {
		List<User> users = userService.findByState(1);
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			user.setPassword(null);
			user.setSalt(null);
		}
		return new ResponseData(ResultCode.SUCCESS, users);
	}

	/**
	 * 保存用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@BehaviorManage(type=AspectType.USER_SAVE)
	public ResponseData saveUser(HttpServletRequest request, HttpServletResponse response,@RequestBody User user) {

		// 密码加密
		new PasswordHelper().encryptPassword(user);
		
		User flush = userService.saveUser(user);
		if (null!=flush) {
			if (null!=flush.getPhoto()) {
				attachmentService.updateState(flush.getPhoto(), 1);
			}
			List<Role> roles = user.getRoles();
			if (null!=roles&&roles.size()>0) {
				List<RoleUser> list = new ArrayList<>();
				for (int i = 0; i < roles.size(); i++) {
					list.add(new RoleUser(roles.get(i).getRoleId(), flush.getUserId()));
				}
				userService.saveRoleUser(flush.getUserId(),list);
			}
		}

		return new ResponseData(ResultCode.SUCCESS, flush.response());
	}
	
	@RequestMapping(value = "validId", method = RequestMethod.POST)
	public ResponseData checkUsrid(HttpServletRequest request, HttpServletResponse response,String id) {

		boolean exists = userService.existsByUserId(id);
		
		if (exists) {
			return new ResponseData(ResultCode.FAILED, id+",已存在");
		}
		return new ResponseData(ResultCode.SUCCESS, "OK");
	}
}
