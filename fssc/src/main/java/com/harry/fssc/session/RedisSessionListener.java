package com.harry.fssc.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * shiro 在redis中的会话监听
 * @author harry
 *
 */
public class RedisSessionListener implements SessionListener{

	private static Logger logger = LoggerFactory.getLogger(RedisSessionListener.class);
	
	@Override
	public void onStart(Session session) {
		logger.debug("会话创建/"+session.getId());
		
	}

	@Override
	public void onStop(Session session) {
		logger.debug("会话停止/"+session.getId());
		
	}

	@Override
	public void onExpiration(Session session) {
		logger.debug("会话过期/"+session.getId());
		
	}

}
