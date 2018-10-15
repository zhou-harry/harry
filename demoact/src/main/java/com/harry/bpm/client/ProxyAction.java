package com.harry.bpm.client;

public abstract class ProxyAction {

	protected boolean checkAccess() {
		System.out.println("执行前的权限检查!");
		return true;
	}

	protected void logUsage() {
		System.out.println("执行后的日志操作!");
	}
}
