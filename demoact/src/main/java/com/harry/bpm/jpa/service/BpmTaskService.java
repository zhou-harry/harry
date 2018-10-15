package com.harry.bpm.jpa.service;

import java.util.List;

import com.harry.bpm.bean.BpmTask;

public interface BpmTaskService extends BaseService<BpmTask> {

	BpmTask findNextTask(String definitionId, String taskId);

	List<BpmTask> findTasks (String definitionId);
	
	Integer countTasks(String definitionId);
}
