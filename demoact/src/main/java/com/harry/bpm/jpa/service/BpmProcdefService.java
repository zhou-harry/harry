package com.harry.bpm.jpa.service;

import java.util.Map;

import com.harry.bpm.bean.BpmProcdef;

public interface BpmProcdefService extends BaseService<BpmProcdef>{

	BpmProcdef findDefinitionIdByMatcher(String procType,Map<String, Object> vars);
	
	BpmProcdef findByKey(String key);
}
