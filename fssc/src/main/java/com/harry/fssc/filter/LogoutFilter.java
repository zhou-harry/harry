package com.harry.fssc.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogoutFilter extends AccessControlFilter {

	private static Logger logger = LoggerFactory.getLogger(LogoutFilter.class);

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		logger.debug("session退出isAccessAllowed...");
		Session session = getSubject(request, response).getSession(false);
		if (session == null) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		logger.debug("session退出onAccessDenied...");
		Session session = getSubject(request, response).getSession(false);
		if (session == null) {
			return true;
		}else {
			try {
				Subject subject = getSubject(request, response);
				subject.logout();// 注销
			} catch (Exception e) {
				throw e;
			}
		}
		return true;
	}

}
