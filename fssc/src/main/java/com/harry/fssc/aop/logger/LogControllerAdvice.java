package com.harry.fssc.aop.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.harry.bpm.exception.BpmException;
import com.harry.bpm.exception.BpmMessage;
import com.harry.fssc.result.ResponseData;

@ControllerAdvice(basePackages="com.harry.fssc.controller")
public class LogControllerAdvice {

	private static Logger LOGGER = LoggerFactory.getLogger(LogControllerAdvice.class);
	private static Logger BPM_LOGGER = LoggerFactory.getLogger(BpmException.class);
	
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(BpmException.class)
	public ResponseData handleBpmException(BpmException e) {
		boolean error = e.getCode().startsWith("E");
		if (error) {
			BPM_LOGGER.error(e.getMessage());
		}else {
			BPM_LOGGER.info(e.getMessage());
		}
		return new ResponseData(e.getCode(),e.getMessage());
	}
}
