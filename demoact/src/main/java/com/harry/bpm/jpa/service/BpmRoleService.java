package com.harry.bpm.jpa.service;

import java.util.List;

import com.harry.bpm.bean.BpmRole;

public interface BpmRoleService extends BaseService<BpmRole> {

	List<String> findRoleIdByTask(String definitionId, String taskId);
	
	List<String> findCopyRoleIdByTask(String definitionId, String taskId);
}
