package com.harry.fssc.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.Filter;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.harry.fssc.cache.RedisCacheManager;
import com.harry.fssc.cookie.UserCookieManager;
import com.harry.fssc.filter.LogoutFilter;
import com.harry.fssc.filter.SessionFilter;
import com.harry.fssc.session.RedisSessionDAO;
import com.harry.fssc.session.RedisSessionListener;
import com.harry.fssc.session.SessionUtil;
import com.harry.fssc.shiro.UserCredentialsMatcher;
import com.harry.fssc.shiro.UserRealm;
import com.harry.fssc.util.Const;
import com.harry.fssc.util.PasswordHelper;

@Configuration
public class ShiroConfig {
	private static Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

	@Resource
	private RedisSessionDAO sessionDAO;

	@Bean
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, SessionUtil sessionUtil) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 必须设置SecuritManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		// 配置访问权限
		LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		// filterChainDefinitionMap.put("/bpm/*", "authc");// 表示需要认证才可以访问
		filterChainDefinitionMap.put("/home/login", "anon");// 表示可以匿名访问
		filterChainDefinitionMap.put("/home/test*", "anon");// 表示可以匿名访问
		filterChainDefinitionMap.put("/home/logout", "home_logout");
		filterChainDefinitionMap.put("/user/*", "sessionFilter");
		filterChainDefinitionMap.put("/bpm/*", "sessionFilter");
		filterChainDefinitionMap.put("/menu/*", "sessionFilter");
		filterChainDefinitionMap.put("/session/*", "sessionFilter");
		filterChainDefinitionMap.put("/redis/*", "sessionFilter");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

		Map<String, Filter> filters = new HashMap<String, Filter>();
		filters.put("anon", new AnonymousFilter());
		filters.put("sessionFilter", new SessionFilter(sessionUtil));
		filters.put("home_logout", new LogoutFilter());
		shiroFilterFactoryBean.setFilters(filters);
		return shiroFilterFactoryBean;
	}

	@Bean
	public RedisCacheManager redisCacheManager() {
		return new RedisCacheManager();
	}

	@Bean
	public SessionManager sessionManager(RedisCacheManager cacheManager) {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setGlobalSessionTimeout(Const.GLOBAL_SESSION_TIMEOUT);
		// session持久化操作
		sessionManager.setSessionDAO(sessionDAO);
		// session缓存方案
//		sessionManager.setCacheManager(cacheManager);
		// session状态监听
		List<SessionListener> sessionListeners = new ArrayList<>();
		sessionListeners.add(new RedisSessionListener());
		sessionManager.setSessionListeners(sessionListeners);
		return sessionManager;
	}

	@Bean
	public SecurityManager securityManager(SessionManager sessionManager, RedisCacheManager cacheManager,
			UserRealm userRealm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setSessionManager(sessionManager);
		securityManager.setCacheManager(cacheManager);
		// 设置user realm.
		securityManager.setRealm(userRealm);
		//注入记住我管理器;
	    securityManager.setRememberMeManager(rememberMeManager());
		return securityManager;
	}

	/**
	 * 身份认证realm;
	 */
	@Bean
	public UserRealm getUserRealm(HashedCredentialsMatcher credentialsMatcher) {
		UserRealm userRealm = new UserRealm();
		// 注入凭证匹配器
		userRealm.setCredentialsMatcher(credentialsMatcher);
		return userRealm;
	}

	/**
	 * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
	 * 所以我们需要修改下doGetAuthenticationInfo中的代码; ）
	 * 
	 * @return
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher(RedisCacheManager cacheManager) {
		HashedCredentialsMatcher hashedCredentialsMatcher = new UserCredentialsMatcher(cacheManager);

		hashedCredentialsMatcher.setHashAlgorithmName(PasswordHelper.algorithm);
		hashedCredentialsMatcher.setHashIterations(PasswordHelper.hashIterations);

		return hashedCredentialsMatcher;
	}

	/**
	 * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;
	 * 
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * cookie对象;
	 * 
	 * @return
	 */
	public SimpleCookie rememberMeCookie() {
		// 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
		SimpleCookie simpleCookie = new SimpleCookie(Const.COOKIE_REMENBERME);
		simpleCookie.setMaxAge(Const.COOKIE_REMENBERME_MAX_AGE);
		return simpleCookie;
	}

	/**
	 * cookie管理对象;记住我功能
	 * 
	 * @return
	 */
	public CookieRememberMeManager rememberMeManager() {
		UserCookieManager cookieRememberMeManager = new UserCookieManager();
		cookieRememberMeManager.setSessionDao(sessionDAO);
		cookieRememberMeManager.setCookie(rememberMeCookie());
		// rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
		cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
		return cookieRememberMeManager;
	}
}
