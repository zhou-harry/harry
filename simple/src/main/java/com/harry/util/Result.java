package com.harry.util;

public class Result {

	@Override
	public String toString() {
		return "Result [status=" + status + ", message=" + message + "]";
	}

	/**
	 * 状态:true:成功,false:失败
	 */
	private Boolean status;

	private String message;

	public Result(Boolean status) {
		this.status = status;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		this.status=false;
	}

}
