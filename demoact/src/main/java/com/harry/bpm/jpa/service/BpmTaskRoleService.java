package com.harry.bpm.jpa.service;

import java.util.List;

import com.harry.bpm.bean.BpmTaskRole;

public interface BpmTaskRoleService extends BaseService<BpmTaskRole>{

	List<BpmTaskRole>findByTask(String taskId);
}
