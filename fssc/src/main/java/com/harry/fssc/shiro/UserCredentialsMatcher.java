package com.harry.fssc.shiro;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harry.fssc.model.User;
import com.harry.fssc.util.Const;
/**
 * 记录登录次数
 * @author harry
 *
 */
public class UserCredentialsMatcher extends HashedCredentialsMatcher {

	private static Logger logger = LoggerFactory.getLogger(UserCredentialsMatcher.class);
	
	private Cache<String, AtomicInteger> passwordRetryCache;

	public UserCredentialsMatcher(CacheManager cacheManager) {
		passwordRetryCache = cacheManager.getCache(Const.PASSWORD_RETYR_CACHE);
	}

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		User user = (User) info.getPrincipals().getPrimaryPrincipal();
		// retry count + 1
		AtomicInteger retryCount = passwordRetryCache.get(user.getSalt());
		if (retryCount == null) {
			retryCount = new AtomicInteger(0);
			passwordRetryCache.put(user.getSalt(), retryCount);
		}
		int count = retryCount.incrementAndGet();
		if (count > Const.MAX_RETRY_COUNT) {
			throw new ExcessiveAttemptsException();
		}
		boolean matches = super.doCredentialsMatch(token, info);
		if (matches) {
			// clear retry count
			passwordRetryCache.remove(user.getSalt());
		}else {
			passwordRetryCache.put(user.getSalt(), retryCount);
		}
		return matches;
	}
}
