package com.harry.bpm.behavior;

import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.task.TaskDefinition;

import com.harry.ActivitiApp;
import com.harry.bpm.enums.TaskResult;
import com.harry.bpm.util.Constant;

public class UserTaskActivityBehaviorImpl extends UserTaskActivityBehavior {

	private static final long serialVersionUID = 1L;
	private String userTaskId;

	public UserTaskActivityBehaviorImpl(String userTaskId, TaskDefinition taskDefinition) {
		super(userTaskId, taskDefinition);
		this.userTaskId = userTaskId;
	}

	@Override
	public void signal(ActivityExecution execution, String signalName, Object signalData) throws Exception {
		super.signal(execution, signalName, signalData);
		
		System.out.println("任务完成后对外的信号窗口..."+execution.getId());
		
//		DynaTaskService dynaTaskService = new DynaTaskService();
//		dynaTaskService
//				.setCommandExecutor(((TaskServiceImpl) execution.getEngineServices().getTaskService())
//						.getCommandExecutor());
//
//		dynaTaskService.updateTaskReason(delegateTask.getId(), result.getStatus());
	}

}
