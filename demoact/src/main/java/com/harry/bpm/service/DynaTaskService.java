package com.harry.bpm.service;

import org.activiti.engine.impl.ServiceImpl;

import com.harry.bpm.cmmand.UpdateTaskReasonCommand;

public class DynaTaskService extends ServiceImpl{

	public void updateTaskReason(String taskId, String deleteReason) {
		
		UpdateTaskReasonCommand dynaTaskCMD = new UpdateTaskReasonCommand(taskId, deleteReason);
		commandExecutor.execute(dynaTaskCMD);

	}

}
