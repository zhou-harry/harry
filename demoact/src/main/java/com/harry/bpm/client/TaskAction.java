package com.harry.bpm.client;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateTask;

public interface TaskAction extends Serializable {

	public void notify(DelegateTask delegateTask);
}
