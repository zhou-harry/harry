package com.harry.fssc.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.harry.fssc.model.User;
import com.harry.fssc.result.ResultCode;
import com.harry.fssc.session.SessionUtil;

public class SessionFilter implements Filter {

	private SessionUtil sessionUtil;
	
	public SessionFilter(SessionUtil sessionUtil) {
		this.sessionUtil=sessionUtil;
	}
	
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) arg0;
		HttpServletResponse httpResponse = (HttpServletResponse) arg1;
		
		boolean authenticated = sessionUtil.isAuthenticated(httpRequest, httpResponse);
		if (!authenticated) {
			httpResponse.setCharacterEncoding("UTF-8");
			httpResponse.setContentType("application/json; charset=utf-8");
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			httpResponse.getWriter().write(ResultCode.USER_SESSION_OUT.getMsg());
			return;
		} else {
			chain.doFilter(arg0, arg1);
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
