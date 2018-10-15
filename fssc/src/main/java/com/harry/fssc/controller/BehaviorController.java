package com.harry.fssc.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.harry.fssc.model.User;
import com.harry.fssc.model.UserBehavior;
import com.harry.fssc.result.ResponseData;
import com.harry.fssc.result.ResultCode;
import com.harry.fssc.service.BehaviorService;
import com.harry.fssc.session.SessionUtil;

@RestController
@RequestMapping("behavior")
public class BehaviorController {

	@Autowired
	private BehaviorService service;
	@Autowired
	private SessionUtil sessionUtil;
	
	@RequestMapping(value = "recentActivities", method = RequestMethod.POST)
	public ResponseData recentActivities(HttpServletRequest request, HttpServletResponse response,String id)
			throws IOException {
//		User info = sessionUtil.getUserInfo(request, response);
		
		List<UserBehavior> list = service.recentActivities(id,request.getRequestedSessionId());
		
		return new ResponseData(ResultCode.SUCCESS, list);
	}
	
	@RequestMapping(value = "allActivities", method = RequestMethod.POST)
	public ResponseData allActivities(HttpServletRequest request, HttpServletResponse response,String id)
			throws IOException {
//		User info = sessionUtil.getUserInfo(request, response);
		
		List<UserBehavior> list = service.allActivities(id);
		
		return new ResponseData(ResultCode.SUCCESS, list);
	}
}
