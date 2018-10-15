package com.harry.fssc.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.harry.fssc.aop.logger.BehaviorManage;
import com.harry.fssc.enums.AspectType;
import com.harry.fssc.model.Role;
import com.harry.fssc.model.RoleMenu;
import com.harry.fssc.result.ResponseData;
import com.harry.fssc.result.ResultCode;
import com.harry.fssc.service.RoleService;
import com.harry.fssc.service.SequenceService;
import com.harry.fssc.util.Const;

@RestController
@RequestMapping("role")
public class RoleController {

	@Autowired
	private RoleService service;
	@Autowired
	private SequenceService sequence;

	@ResponseBody
	@RequestMapping(value = "info", method = RequestMethod.POST)
	public ResponseData getInfo(HttpServletRequest request, HttpServletResponse response, String roleid) {
		Role role = service.findRole(roleid);
		if (null != role) {
			return new ResponseData(ResultCode.SUCCESS, role);
		}
		return new ResponseData(ResultCode.FAILED, null);
	}

	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public ResponseData findUsers(HttpServletRequest request, HttpServletResponse response) {
		List<Role> list = service.findAll();
		return new ResponseData(ResultCode.SUCCESS, list);
	}
	
	@ResponseBody
	@RequestMapping(value = "listByUser", method = RequestMethod.POST)
	public ResponseData findUsers(HttpServletRequest request, HttpServletResponse response,String userid) {
		List<Role> list = service.findByUser(userid);
		return new ResponseData(ResultCode.SUCCESS, list);
	}

	/**
	 * 保存
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@BehaviorManage(type = AspectType.ROLE_SAVE)
	public ResponseData save(HttpServletRequest request, HttpServletResponse response, @RequestBody Role role) {

		String roleId = role.getRoleId();
		if (null==roleId) {
			roleId = sequence.getNextVal(Const.SEQ_ROLEID);
			role.setRoleId(roleId);
		}
		Role saveRole = service.saveRole(role);

		return new ResponseData(ResultCode.SUCCESS, saveRole);
	}
	
	@RequestMapping(value = "remove", method = RequestMethod.POST)
	@BehaviorManage(type = AspectType.ROLE_REMOVE)
	public ResponseData remove(HttpServletRequest request, HttpServletResponse response, String roleid) {

		service.deleteRole(roleid);

		return new ResponseData(ResultCode.SUCCESS, roleid+"，删除成功。");
	}
	
	@RequestMapping(value = "saveRM", method = RequestMethod.POST)
	@BehaviorManage(type = AspectType.ROLE_SAVE)
	public ResponseData saveRoleMenu(HttpServletRequest request, HttpServletResponse response, @RequestBody List<RoleMenu> list) {

		if (null!=list&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				RoleMenu rm = list.get(i);
				if (rm.getValid()) {
					service.saveRoleMenu(rm);
				}else {
					service.deleteRoleMenu(rm);
				}
			}
			return new ResponseData(ResultCode.SUCCESS, "操作成功！");
		}
		return new ResponseData(ResultCode.FAILED, "数据为空！");
	}
}
