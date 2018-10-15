package com.harry.fssc.cookie;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harry.fssc.model.User;
import com.harry.fssc.session.RedisSessionDAO;

public class UserCookieManager extends CookieRememberMeManager {

	private static Logger logger = LoggerFactory.getLogger(UserCookieManager.class);
	
    private RedisSessionDAO sessionDAO;
    
    public void setSessionDao(RedisSessionDAO sessionDAO) {
		this.sessionDAO=sessionDAO;
	}
	
	@Override
	public void onSuccessfulLogin(Subject subject, AuthenticationToken token, AuthenticationInfo info) {
//		sessionDAO.doCreateCookie((User)subject.getPrincipal());
		super.onSuccessfulLogin(subject, token, info);
	}

}
