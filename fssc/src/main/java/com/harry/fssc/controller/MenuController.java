package com.harry.fssc.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.harry.fssc.cache.RedisCacheManager;
import com.harry.fssc.model.SideContent;
import com.harry.fssc.model.User;
import com.harry.fssc.result.ResponseData;
import com.harry.fssc.result.ResultCode;
import com.harry.fssc.service.BpmService;
import com.harry.fssc.service.MenuService;
import com.harry.fssc.session.SessionUtil;
import com.harry.fssc.util.RedisUtil;

@RestController
@RequestMapping("menu")
public class MenuController {

	private static Logger logger = LoggerFactory.getLogger(MenuController.class);

	@Autowired
	private MenuService service;
	@Autowired
	private BpmService bpmService;
	@Autowired
	private RedisCacheManager cacheManager;

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private SessionUtil sessionUtil;

	@RequestMapping(value = "initSide", method = RequestMethod.POST)
	public ResponseData initSideContent(HttpServletRequest request, HttpServletResponse response, String parentKey) {
		
		User userInfo = sessionUtil.getUserInfo(request, response);
		
		List<SideContent> list = service.initSideContent(userInfo.getUserId(),parentKey);

		Map<String, List<SideContent>> result = new HashMap<>();
		List<SideContent> navigation = new ArrayList<>();
		List<SideContent> fixedNavigation = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			SideContent sideContent = list.get(i);
			if (sideContent.getFixed()) {
				fixedNavigation.add(sideContent);
			} else {
				navigation.add(sideContent);
			}
		}
		result.put("navigation", navigation);
		result.put("fixedNavigation", fixedNavigation);

		return new ResponseData(ResultCode.SUCCESS, result);

	}
	
	@RequestMapping(value = "treeSide", method = RequestMethod.POST)
	public ResponseData treeSide(HttpServletRequest request, HttpServletResponse response, String roleId, String parentKey) {
		
		List<HashMap<String, Object>> list = service.findWithRole(roleId, parentKey);
		
		for (int i = 0; i < list.size(); i++) {
			HashMap<String, Object> map = list.get(i);
			String key = (String)map.get("KEY");
			map.put("items", service.findWithRole(roleId, key));
		}

		return new ResponseData(ResultCode.SUCCESS, list);

	}
	
	@RequestMapping(value = "forms", method = RequestMethod.POST)
	public ResponseData findByForm(HttpServletRequest request, HttpServletResponse response) {
		
		List<Map<String, Object>> result = new ArrayList<>();
		List<SideContent> list = service.findByForm(true);
		
		for (int i = 0; i < list.size(); i++) {
			SideContent m = list.get(i);
			Map<String, Object> f = new HashMap<>();
			f.put("icon", m.getIcon());
			f.put("title", m.getTitle());
			f.put("key", m.getKey());
			result.add(f);
		}
		return new ResponseData(ResultCode.SUCCESS, result);

	}
	
	@RequestMapping(value = "form", method = RequestMethod.POST)
	public ResponseData findByKey(HttpServletRequest request, HttpServletResponse response,String key) {
		
		SideContent content = service.findByKey(key);
		
		return new ResponseData(ResultCode.SUCCESS, content);

	}

	@RequestMapping(value = "monitor", method = RequestMethod.POST)
	public ResponseData monitorSystem(HttpServletRequest request, HttpServletResponse response) {

		List<Map<String, Object>> result = new ArrayList<>();

		List<SideContent> list = service.monitorSystem();
		Map<String, Object> input;
		for (int i = 0; i < list.size(); i++) {
			SideContent content = list.get(i);
			input = new HashMap<>();
			input.put("key", content.getKey());
			input.put("title", content.getTitle());
			input.put("icon", content.getIcon());
			if ("sessionManager".equals(content.getKey())) {
				input.put("number", cacheManager.size());
				input.put("unit", "sessionManager".equals(content.getKey())?"在线用户数":null);
			}else if ("redisManager".equals(content.getKey())) {
				Map<String, Object>port=new HashMap<>();
				redisUtil.infos(redisUtil.redisInfo("clients"),port);
				input.put("number", port.size());
				input.put("unit", "Redis集群数量");
			}
			input.put("info", "正常");
			input.put("infoState", "Success");
			result.add(input);
		}
		return new ResponseData(ResultCode.SUCCESS, result);

	}
}
