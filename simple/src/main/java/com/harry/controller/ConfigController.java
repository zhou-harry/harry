package com.harry.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harry.entity.Role;
import com.harry.entity.Tree;
import com.harry.service.RoleService;
import com.harry.util.Result;
import com.harry.util.json.JsonUtil;
import com.harry.util.session.SessionProvider;

@Controller
@RequestMapping("config")
public class ConfigController {
	private static Logger logger = Logger.getLogger(ConfigController.class);

	@Resource
	private RoleService roleService;
	@Resource
	private SessionProvider session;

	@ResponseBody
	@RequestMapping(value = "roleInfo", produces = "application/json;charset=UTF-8")
	public String roleInfo(HttpServletRequest request, String role) {
		Role roleData = JsonUtil.json2Object(role, Role.class);
		List<Role> list = roleService.queryRole(roleData);
		String result = JsonUtil.list2Json(list);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "roleFilter", produces = "application/json;charset=UTF-8")
	public String roleFilter(HttpServletRequest request, String filter) {
		List<Role> list = roleService.queryRole(JsonUtil.decode(filter));
		String result = JsonUtil.list2Json(list);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "roleSave", produces = "application/json;charset=UTF-8")
	public String roleSave(HttpServletRequest request, String role) {
		Role roleData = JsonUtil.json2Object(role, Role.class);
		Result result = roleService.saveRole(roleData);
		return JsonUtil.obj2Json(result);
	}
	
	@ResponseBody
	@RequestMapping(value = "roleTree", produces = "application/json;charset=UTF-8")
	public String roleTree(HttpServletRequest request) {
		List<Tree> list = roleService.getRoleTree();
		return JsonUtil.list2Json(list);
	}
}
