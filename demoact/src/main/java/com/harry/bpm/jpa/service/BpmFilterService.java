package com.harry.bpm.jpa.service;

import java.util.List;
import java.util.Map;

import com.harry.bpm.bean.BpmFilter;

public interface BpmFilterService extends BaseService<BpmFilter> {
	
	public BpmFilter findFilterBykey(String filterId);

	public Map<String, Object> findByKey(String key);
	
	public List<BpmFilter> findForRelation(String key);
	
	public String findNameByKey(String filterId);
}
