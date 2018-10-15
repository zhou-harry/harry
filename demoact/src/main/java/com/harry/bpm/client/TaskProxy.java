package com.harry.bpm.client;

import org.activiti.engine.delegate.DelegateTask;

public class TaskProxy extends ProxyAction implements TaskAction {

	private static final long serialVersionUID = 1L;

	private TaskAction action;

	public TaskProxy(TaskAction action) {
		this.action = action;
	}

	public void notify(DelegateTask delegateTask) {
		if (null == action) {
			return;
		}
		boolean access = this.checkAccess();
		if (access) {
			action.notify(delegateTask);
		}
		this.logUsage();
	}
}
