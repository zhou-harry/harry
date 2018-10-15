package com.harry.fssc.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.harry.fssc.aop.logger.BehaviorManage;
import com.harry.fssc.aop.logger.LoggerManage;
import com.harry.fssc.enums.AspectType;
import com.harry.fssc.result.ResponseData;
import com.harry.fssc.result.ResultCode;
import com.harry.fssc.util.RedisUtil;

@RestController
@RequestMapping("redis")
public class RedisController {

	@Autowired
	private RedisUtil redisUtil;
	
	@ResponseBody
	@RequestMapping(value = "/info", method = RequestMethod.POST)
	public ResponseData infoRedis(HttpServletRequest request, HttpServletResponse response){
		Map<Object, Object>result=new HashMap<>();
		Map<String, Object>port=new HashMap<>();
		
		result.put("states", redisUtil.infos(redisUtil.redisInfo("stats"),port));
		
		result.put("clients", redisUtil.infos(redisUtil.redisInfo("clients"),port));
		
		result.put("server", redisUtil.infos(redisUtil.redisInfo("server"),port));
		
		result.put("memory", redisUtil.infos(redisUtil.redisInfo("memory"),port));
		
		result.put("cpu", redisUtil.infos(redisUtil.redisInfo("cpu"),port));
		
		result.put("replication", redisUtil.infos(redisUtil.redisInfo("replication"),port));
		
		result.put("persistence", redisUtil.infos(redisUtil.redisInfo("persistence"),port));
		
		result.put("nodes", port);
		
		
		return new ResponseData(ResultCode.SUCCESS, result);
	}
	
}
