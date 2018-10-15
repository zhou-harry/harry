package com.harry.bpm.jpa.service;

import java.util.List;

import com.harry.bpm.bean.BpmOwner;

public interface BpmOwnerService extends BaseService<BpmOwner> {

	List<String> findOwnerIdByTask(String definitionId, String taskId);
	
	List<String> findOwnerIdByRole(String roleId);
	
	List<BpmOwner> findOwnerByRole(String roleid);
	
	Integer countOwnerByRole(String roleid);
	
	void deleteByRole(String roleid);
}
