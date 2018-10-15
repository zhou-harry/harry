package com.harry.bpm.exception;

import java.text.MessageFormat;

public class BpmException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private String code;
	private String message;
	
	public BpmException(BpmMessage message,RuntimeException e) {
		super(e);
		this.code=message.getCode();
		this.message=message.getName();
	}
	
	public BpmException(BpmMessage message,String... s) {
		this.code=message.getCode();
		this.message= MessageFormat.format(message.getName(), s);
	}
	
	public BpmException(BpmMessage message,RuntimeException e,String... s) {
		super(e);
		this.code=message.getCode();
		this.message= MessageFormat.format(message.getName(), s);
	}

	public String showMessage() {
		return "BpmException [code=" + code + ", message=" + message + "]";
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	
}
