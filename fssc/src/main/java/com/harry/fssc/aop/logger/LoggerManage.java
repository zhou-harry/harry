package com.harry.fssc.aop.logger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.harry.fssc.enums.AspectType;

/**
 * @Description: 日志注解
 * @author harry
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoggerManage {

	public AspectType description();
}
