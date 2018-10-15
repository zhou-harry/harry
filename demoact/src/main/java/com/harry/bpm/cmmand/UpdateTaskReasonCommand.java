package com.harry.bpm.cmmand;

import java.io.Serializable;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;

public class UpdateTaskReasonCommand implements Command<Void>, Serializable {

	protected String taskId;
	protected String deleteReason;

	public UpdateTaskReasonCommand(String taskId, String deleteReason) {
		this.taskId = taskId;
		this.deleteReason = deleteReason;
	}

	public Void execute(CommandContext commandContext) {
		HistoricTaskInstanceEntity historicTaskInstance = commandContext.getDbSqlSession()
				.selectById(HistoricTaskInstanceEntity.class, taskId);
		if (historicTaskInstance != null) {
			System.out.println(taskId+"=======更新===="+deleteReason);
			historicTaskInstance.markEnded(deleteReason);
			historicTaskInstance.setDescription(taskId+"=======更新===="+deleteReason);
			
			commandContext.getDbSqlSession().update(historicTaskInstance);
		}
		return null;
	}

}
