package com.harry.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harry.entity.Menu;
import com.harry.entity.User;
import com.harry.service.MenuService;
import com.harry.util.Const;
import com.harry.util.json.JsonUtil;
import com.harry.util.session.SessionProvider;

@Controller
@RequestMapping("menu")
public class MenuController {
	private static Logger logger = Logger.getLogger(MenuController.class);
	@Resource
	private MenuService menuService;
	@Resource
	private SessionProvider session;

	@ResponseBody
	@RequestMapping(value = "menuByParent", produces = "application/json;charset=UTF-8")
	public String getMenuByParent(HttpServletRequest request, String parentId)
			throws IOException {
		User user = (User) session.getAttribute(request, Const.SESSION_USER);
		try {
			List<Menu> list = menuService.getMenuByParent(user.getId(),
					parentId);
			return JsonUtil.list2Json(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
