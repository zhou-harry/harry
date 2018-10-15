package com.harry.fssc.aop.logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harry.fssc.enums.AspectType;
import com.harry.fssc.model.User;
import com.harry.fssc.model.UserBehavior;
import com.harry.fssc.result.ResponseData;
import com.harry.fssc.service.BehaviorService;
import com.harry.fssc.session.SessionUtil;

/**
 * @Description: 日志管理
 */
@Aspect
@Service
public class LoggerAdvice {
	
	private Logger logger = LoggerFactory.getLogger(LoggerAdvice.class);
	
	@Autowired
	private SessionUtil sessionUtil;
	@Autowired
	private BehaviorService behaviorService;

	@Before("within(com.harry.fssc..*) && @annotation(loggerManage)")
	public void addBeforeLogger(JoinPoint joinPoint, LoggerManage loggerManage) {
		logger.info("执行 " + loggerManage.description() + " 开始");
		logger.info(joinPoint.getSignature().toString());
		logger.info(parseParames(joinPoint.getArgs()));
	}
	
	@AfterReturning(value="within(com.harry.fssc..*) && @annotation(loggerManage)",returning="result")
	public void addAfterReturningLogger(JoinPoint pjp, LoggerManage loggerManage,ResponseData result) throws Throwable {
		HttpServletRequest request = getRequest(pjp.getArgs());
		logger.info("执行 " + loggerManage.description() + " 结束，"+result.toString()
		+"RequestedSessionId: "+request.getRequestedSessionId()
		+"sessionId: "+request.getSession().getId()
		);
	}
	@Around("within(com.harry.fssc..*) && @annotation(behavior)")
	public Object recordBehavior(ProceedingJoinPoint pjp, BehaviorManage behavior) throws Throwable {
		HttpServletRequest request = getRequest(pjp.getArgs());
		HttpServletResponse response = getResponse(pjp.getArgs());
		AspectType type = behavior.type();
		Object result = pjp.proceed();
		String sessionId = null;
		switch (type) {
		case LOGOUT:
			sessionId=request.getRequestedSessionId();
			break;
		default:
			sessionId=request.getSession().getId();
			break;
		}
		User sessionUsr = sessionUtil.getUserInfo(request, response);
		behaviorService.save(new UserBehavior(null==sessionUsr?null:sessionUsr.getUserId(), sessionId,
				type.getKey(), type.getName(), request.getRemoteAddr(),request.getRemoteHost(),result.toString()));
		return result;
	}
	
	@AfterThrowing(pointcut = "within(com.harry.fssc..*) && @annotation(loggerManage)", throwing = "ex")
	public void addAfterThrowingLogger(JoinPoint joinPoint, LoggerManage loggerManage, Exception ex) {
		HttpServletRequest request = getRequest(joinPoint.getArgs());
		HttpServletResponse response = getResponse(joinPoint.getArgs());
		User sessionUsr = sessionUtil.getUserInfo(request, response);
		AspectType type = loggerManage.description();
		
		behaviorService.save(new UserBehavior(null==sessionUsr?null:sessionUsr.getUserId(), request.getSession().getId(),
				type.getKey(), type.getName(), request.getRemoteAddr(),request.getRemoteHost(),ex.getMessage()));
	}
	
	private HttpServletRequest getRequest(Object[] parames) {
		if (null == parames || parames.length <= 0) {
			return null;
		}
		for (Object obj : parames) {
			if (obj instanceof HttpServletRequest) {
				return (HttpServletRequest)obj;
			}
		}
		return null;
	}
	
	private HttpServletResponse getResponse(Object[] parames) {
		if (null == parames || parames.length <= 0) {
			return null;
		}
		for (Object obj : parames) {
			if (obj instanceof HttpServletResponse) {
				return (HttpServletResponse)obj;
			}
		}
		return null;
	}

	private String parseParames(Object[] parames) {
		if (null == parames || parames.length <= 0) {
			return "";
		}
		StringBuffer param = new StringBuffer("传入参数[{}] ");
		for (Object obj : parames) {
			param.append(ToStringBuilder.reflectionToString(obj)).append("  ");
		}
		return param.toString();
	}
	
}
