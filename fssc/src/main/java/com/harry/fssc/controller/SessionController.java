package com.harry.fssc.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.harry.fssc.aop.logger.BehaviorManage;
import com.harry.fssc.aop.logger.LoggerManage;
import com.harry.fssc.enums.AspectType;
import com.harry.fssc.model.User;
import com.harry.fssc.result.ResponseData;
import com.harry.fssc.result.ResultCode;
import com.harry.fssc.session.SessionUtil;
import com.harry.fssc.util.Const;

@RestController
@RequestMapping("session")
public class SessionController {

	private static Logger logger = LoggerFactory.getLogger(SessionController.class);
	@Autowired
	private SessionUtil sessionUtil;
	
	@ResponseBody
	@RequestMapping(value = "/forceLogout", method = RequestMethod.POST)
	@BehaviorManage(type=AspectType.LOGOUT_FORCE)
	public ResponseData forceLogout(HttpServletRequest request, HttpServletResponse response,String sessionId){
		//注销
		ResultCode resultCode = sessionUtil.logout(sessionId);
		return new ResponseData(resultCode, "OK");
	}
	
	@ResponseBody
	@RequestMapping(value = "/sessionList", method = RequestMethod.POST)
	public ResponseData sessionList(HttpServletRequest request, HttpServletResponse response){
		Collection<Session> list = sessionUtil.sessionList();
		List<Map<String, Object>>result=new ArrayList<>();
		Map<String, Object>input;
		for (Session session : list) {
			if (null==session) {
				continue;
			}
			input=new HashMap<>();
			input.put("id", session.getId());
			input.put("host", session.getHost());//
			input.put("start", session.getStartTimestamp());
			input.put("lastAccess", session.getLastAccessTime());
			input.put("times", session.getTimeout());
			Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
			if (null!=obj) {
				SimplePrincipalCollection coll = (SimplePrincipalCollection) obj;
				User user=(User) coll.getPrimaryPrincipal();
				input.put("name", user.getName());
				//当前session状态
				boolean authenticated = sessionUtil.isAuthenticated(request, response,session.getId());
				input.put("active", authenticated);
			}
			result.add(input);
		}
		return new ResponseData(ResultCode.SUCCESS, result);
	}
	
	@ResponseBody
	@RequestMapping(value = "/currentCookie", method = RequestMethod.POST)
	public ResponseData currentCookie(HttpServletRequest request, HttpServletResponse response){
		User info = sessionUtil.getUserInfo(request, response);
		if (null!=info) {
			return new ResponseData(ResultCode.SUCCESS,info.response());
		}else {
			return new ResponseData(ResultCode.FAILED);
		}
	}
}
