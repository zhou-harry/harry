package com.harry.fssc.util;

public enum UserState {
	invalid(0, "未激活"), valid(1, "正常"), locked(2, "锁定");

	UserState(Integer status, String name) {
		this.status = status;
		this.name = name;
	}

	private Integer status;
	private String name;

	public String getName() {
		return name;
	}

	public Integer getStatus() {
		return status;
	}
}
