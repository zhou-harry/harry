package com.harry.fssc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.harry.fssc.result.ResultCode;
import com.harry.fssc.session.SessionUtil;

public class UserSecurityInterceptor implements HandlerInterceptor {

	private static Logger loger = LoggerFactory.getLogger(UserSecurityInterceptor.class);

	@Autowired
	private SessionUtil sessionUtil;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// loger.debug("用户安全拦截器:在Controller执行之前调用，如果返回false，controller不执行"+handler);

		boolean authenticated = sessionUtil.isAuthenticated(request, response);
		ResultCode resultCode = authenticated ? ResultCode.SUCCESS : ResultCode.USER_SESSION_OUT;

		if (!authenticated) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			response.getWriter().write(resultCode.getMsg());
		}
		return authenticated;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// loger.debug("用户安全拦截器:controller执行之后，且页面渲染之前调用"+handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// loger.debug("用户安全拦截器:页面渲染之后调用，一般用于资源清理操作"+handler);
	}
}
