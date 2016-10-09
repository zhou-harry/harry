package com.harry.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.harry.entity.User;
import com.harry.service.UserService;
import com.harry.util.Const;
import com.harry.util.Result;
import com.harry.util.Tools;
import com.harry.util.json.JsonUtil;
import com.harry.util.session.SessionProvider;

@Controller
@RequestMapping("user")
public class UserController {
	private static Logger logger = Logger.getLogger(UserController.class);
	@Resource
	private UserService userService;
	@Resource
	private SessionProvider session;
	
	@ResponseBody
	@RequestMapping(value="login",produces="application/json;charset=UTF-8")
	public String login(HttpServletRequest request,
			HttpServletResponse response,
			@Validated @ModelAttribute("user") User user,
			BindingResult bindingResult) throws IOException {
		logger.info("=============login==============="+user);
		Result result=new Result(true);
		if (bindingResult.hasErrors()) {
			List<FieldError> errors = bindingResult.getFieldErrors();
			for (FieldError error : errors) {
				logger.info(error.getField() + " : "
						+ error.getDefaultMessage());
			}
			return JsonUtil.obj2Json(result);
		}
		if (null==user||Tools.isEmpty(user.getUserName())||Tools.isEmpty(user.getPassword())) {
			result.setMessage("账号无效!");
			return JsonUtil.obj2Json(result);
		}
		User currentUser=null;
		try {
			currentUser = userService.getUser(user);
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			logger.error("用户:"+user.getUserName()+"登录失败!");
		}catch (Exception e) {
			e.printStackTrace();
			result.setMessage("用户:"+user.getUserName()+"登录失败!");
		}
		if (null != currentUser) {
			//更新登录信息
			userService.updateLoginInfo(currentUser);
			session.setAttribute(request, response, Const.SESSION_USER, currentUser);
			result.setStatus(true);
		}else {
			result.setMessage("用户名或密码不正确,登录失败!");
		}
		return JsonUtil.obj2Json(result);
	}
	
	@ResponseBody
	@RequestMapping(value="sessionUser",produces="application/json;charset=UTF-8")
	public String sessionUser(HttpServletRequest request) throws IOException {
		User user = (User)session.getAttribute(request, Const.SESSION_USER);
		
		return JsonUtil.obj2Json(user);
	}
	
	@RequestMapping("logout")
	public void logout(HttpServletRequest request,HttpServletResponse response)throws Exception {
		session.logout(request,response);
		
		response.sendRedirect(request.getContextPath() + Const.LOGIN);
	}
}
