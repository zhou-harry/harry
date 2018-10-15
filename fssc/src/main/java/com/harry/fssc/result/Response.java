package com.harry.fssc.result;

public class Response {
	/** 返回信息码 */
	private String statusCode = "000000";
	/** 返回信息内容 */
	private String message = "操作成功";

	public Response() {
	}

	public Response(ResultCode msg) {
		this.statusCode = msg.getCode();
		this.message = msg.getMsg();
	}

	public Response(String statusCode) {
		this.statusCode = statusCode;
		this.message = "";
	}

	public Response(String statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}

	public String getstatusCode() {
		return statusCode;
	}

	public void setstatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getmessage() {
		return message;
	}

	public void setmessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Response [statusCode=" + statusCode + ", message=" + message + "]";
	}
	
	
}
