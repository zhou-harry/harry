package com.harry.fssc.session;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.harry.fssc.model.User;
import com.harry.fssc.util.Const;

import io.lettuce.core.codec.StringCodec;

/**
 * redis实现共享session
 */
@Component
public class RedisSessionDAO extends EnterpriseCacheSessionDAO {

	private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	// 创建session，保存到数据库
	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = super.doCreate(session);
		logger.debug("创建session:{}", session.getId());
		redisTemplate.opsForValue().set(Const.REDIS_SESSION_PREFIX + sessionId.toString(), session);
		return sessionId;
	}

	// 获取session
	@Override
	protected Session doReadSession(Serializable sessionId) {
		logger.debug("获取session:{}", sessionId);
		// 先从缓存中获取session，如果没有再去数据库中获取
		Session session = super.doReadSession(sessionId);
		if (session == null) {
			session = (Session) redisTemplate.opsForValue().get(Const.REDIS_SESSION_PREFIX + sessionId.toString());
		}
		return session;
	}

	// 更新session的最后一次访问时间
	@Override
	protected void doUpdate(Session session) {
		super.doUpdate(session);
		logger.debug("更新session:{}", session.getId());
		String key = Const.REDIS_SESSION_PREFIX + session.getId().toString();
		if (!redisTemplate.hasKey(key)) {
			redisTemplate.opsForValue().set(key, session);
		}
		redisTemplate.expire(key, Const.GLOBAL_SESSION_TIMEOUT, TimeUnit.MILLISECONDS);
	}

	// 删除session
	@Override
	protected void doDelete(Session session) {
		logger.debug("删除session:{}", session.getId());
		super.doDelete(session);
		redisTemplate.delete(Const.REDIS_SESSION_PREFIX + session.getId().toString());
	}
	/**
	 * 将当前用户放入缓存
	 * @param user
	 */
	public void doCreateCookie(User user) {
		String key=Const.COOKIE_REMENBERME + user.getCredentialsSalt();
		redisTemplate.opsForValue().set(key, user);
		if (Const.COOKIE_REMENBERME_MAX_AGE>0) {
			redisTemplate.expire(key, Const.COOKIE_REMENBERME_MAX_AGE, TimeUnit.MILLISECONDS);
		}
	}
	
}
