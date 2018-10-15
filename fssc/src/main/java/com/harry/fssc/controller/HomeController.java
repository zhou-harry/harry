package com.harry.fssc.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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
import com.harry.fssc.model.User;
import com.harry.fssc.result.ResponseData;
import com.harry.fssc.result.ResultCode;
import com.harry.fssc.session.SessionUtil;

@RestController
@RequestMapping("home")
public class HomeController {
	private static Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private SessionUtil sessionUtil;
	
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@BehaviorManage(type=AspectType.LOGIN)
	@LoggerManage(description=AspectType.LOGIN)
	public ResponseData login(HttpServletRequest request, HttpServletResponse response, @RequestBody User user)
			throws ParseException {
		String msg = null, loginName = user.getUserId();
		boolean rememberMe=true;
		UsernamePasswordToken token = new UsernamePasswordToken(loginName, user.getPassword());
		Subject subject = SecurityUtils.getSubject();
		try {
			token.setRememberMe(true);
			subject.login(token);
			//踢出当前用户的其它会话
			sessionUtil.kickout(subject);
		} catch (UnknownAccountException uae) {
			logger.debug("对用户[" + loginName + "]进行登录验证..验证未通过,未知账户");
			msg = "未知账户";
		} catch (IncorrectCredentialsException ice) {
			logger.debug("对用户[" + loginName + "]进行登录验证..验证未通过,错误的凭证");
			msg = "用户名 / 密码错误";
		} catch (LockedAccountException lae) {
			logger.debug("对用户[" + loginName + "]进行登录验证..验证未通过,账户已锁定");
			msg = "账户已锁定";
		} catch (DisabledAccountException lae) {
			logger.debug("对用户[" + loginName + "]进行登录验证..验证未通过,帐户未激活");
			msg = "帐户未激活";
		} catch (ExcessiveAttemptsException eae) {
			logger.debug("对用户[" + loginName + "]进行登录验证..验证未通过,错误次数过多");
			msg = "错误次数过多";
		} catch (AuthenticationException ae) { // 通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
			logger.debug("对用户[" + loginName + "]进行登录验证..验证未通过");
			msg = "验证未通过";
			ae.printStackTrace();
		}
		if (msg != null) {// 登录失败
			return new ResponseData(ResultCode.FAILED, msg);
		} else {// 登录成功
			if (subject.isAuthenticated()) {
				User currentUser = (User) subject.getPrincipal();
				return new ResponseData(ResultCode.SUCCESS, currentUser.response());
			} else {
				return new ResponseData(ResultCode.FAILED, "验证未通过");
			}
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	@BehaviorManage(type=AspectType.LOGOUT)
	public ResponseData login(HttpServletRequest request, HttpServletResponse response){
		//此处清理业务缓存
		return new ResponseData(ResultCode.SUCCESS, "OK");
	}
	
}
