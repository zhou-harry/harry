package com.harry.bpm.client;

import org.activiti.engine.delegate.DelegateExecution;

public class ExecutionProxy extends ProxyAction implements ExecutionAction {

	private static final long serialVersionUID = 1L;
	
	private ExecutionAction action;

	public ExecutionProxy(ExecutionAction action) {
		this.action = action;
	}

	public void notify(DelegateExecution execution) {
		if (null == action) {
			return;
		}
		boolean access = this.checkAccess();
		if (access) {
			action.notify(execution);
		}
		this.logUsage();
	}

}
