package com.harry.fssc.listener.bpm;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.runtime.ProcessInstance;

import com.harry.bpm.client.ExecutionAction;

public class ExclusiveReceive implements ExecutionAction {

	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) {

		System.out.println("client: 流程处于等待状态,流程模板:[" + execution.getProcessDefinitionId() + "],流程号:["
				+ execution.getProcessInstanceId() + "],业务号:[" + execution.getProcessBusinessKey() + "],执行id:["
				+ execution.getId() + "]");

	}

}
