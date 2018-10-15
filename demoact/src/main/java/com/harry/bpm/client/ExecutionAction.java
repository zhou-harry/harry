package com.harry.bpm.client;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateExecution;

public interface ExecutionAction extends Serializable {

	public void notify(DelegateExecution execution);
}
