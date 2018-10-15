package com.harry.fssc.result;

public enum ResultCode {
	
	SUCCESS("000000", "操作成功"), 
	FAILED("999999", "操作失败"), 
    USER_NOT_EXISTS("000001","该用户未注册"),
    USER_SESSION_OUT("000004","连接超时，请重新登录!"),
	PARAMETER_ERROR("000002", "参数错误"),
	PERMISSION_DENIED("000003", "权限不足"),
	CURRENT_SESSION_OUT("000005","不能强制退出当前会话!");
	
	private ResultCode(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private String code;
	private String msg;

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
