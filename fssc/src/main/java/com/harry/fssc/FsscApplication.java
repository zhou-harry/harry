package com.harry.fssc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class FsscApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(FsscApplication.class, args);
	}
	
	/**
	 * 此处支持外部容器部署
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(FsscApplication.class);
	}
}
