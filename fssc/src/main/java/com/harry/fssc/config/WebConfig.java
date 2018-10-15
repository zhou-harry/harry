package com.harry.fssc.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.mgt.SecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.harry.fssc.interceptor.UserSecurityInterceptor;
import com.harry.fssc.session.SessionUtil;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Bean
	public SessionUtil initSessionUtil(SecurityManager securityManager) {
		return new SessionUtil(securityManager);
	}

	@Bean
	UserSecurityInterceptor initUserSecurityInterceptor() {
		return new UserSecurityInterceptor();
	}

	/**
	 * spring管理拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> include = new ArrayList<>();
		include.add("/user/*");
		include.add("/bpm/*");
		include.add("/menu/*");
		List<String> exclude = new ArrayList<>();
		exclude.add("/home/*");

		registry.addInterceptor(initUserSecurityInterceptor()).addPathPatterns(include).excludePathPatterns(exclude);

		WebMvcConfigurer.super.addInterceptors(registry);
	}

	/**
	 * 此处不再通过spring管理filter了,交给shiro管理,否则filter中不能获取shiro session
	 */
//	@Bean
//	public FilterRegistrationBean<SessionFilter> sessionFilterBean() {
//		FilterRegistrationBean<SessionFilter> registration = new FilterRegistrationBean<SessionFilter>();
//		registration.setName("SessionFilter");
//		registration.setFilter(initSessionFilter());
//		List<String> urlPatterns = new ArrayList<String>();
//		urlPatterns.add("/user/*");
//		urlPatterns.add("/bpm/*");
//		urlPatterns.add("/menu/*");
//		registration.setUrlPatterns(urlPatterns);
//		registration.setOrder(1);
//		return registration;
//	}
}
