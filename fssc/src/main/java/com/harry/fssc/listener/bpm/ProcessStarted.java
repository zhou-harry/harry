package com.harry.fssc.listener.bpm;

import org.activiti.engine.delegate.DelegateExecution;

import com.harry.bpm.client.ExecutionAction;

public class ProcessStarted implements ExecutionAction {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void notify(DelegateExecution execution) {
		
		System.out.println("client: 流程启动成功,流程模板:[" + execution.getProcessDefinitionId() + "],流程号:["
				+ execution.getProcessInstanceId() + "],业务号:[" + execution.getProcessBusinessKey() + "],执行id:["
				+ execution.getId() + "]");
	}

}
