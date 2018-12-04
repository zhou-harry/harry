package com.harry.fssc.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.mgt.SecurityManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
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
	 * 处理跨域访问
	 * @return
	 */
	@Bean
	public FilterRegistrationBean corsFilter() {
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    CorsConfiguration config = new CorsConfiguration();
	    config.setAllowCredentials(true);	
	    config.addAllowedOrigin("*");//http://localhost:8086
	    config.addAllowedOrigin("null");
	    config.addAllowedHeader("*");
	    config.addAllowedMethod("*");
	    source.registerCorsConfiguration("/**", config); // CORS 配置对所有接口都有效
	    FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
	    bean.setOrder(0);
	    return bean;
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
