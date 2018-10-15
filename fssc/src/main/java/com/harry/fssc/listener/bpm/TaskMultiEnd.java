package com.harry.fssc.listener.bpm;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.ProcessInstance;

import com.harry.bpm.client.TaskAction;

public class TaskMultiEnd implements TaskAction {

	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask task) {

		ExecutionEntity pi = ((ExecutionEntity)task.getExecution()).getProcessInstance();

		System.out.println("client: 会签任务完成,流程号:[" + pi.getId() + "],业务号:["
				+ pi.getBusinessKey() + "],流程定义id:[" + pi.getName() + "]任务实例id[" + task.getId() + "],任务名称:["
				+ task.getName() + "]");

	}

}
