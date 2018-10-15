package com.harry.bpm.jpa.service;

import java.util.List;

import com.harry.bpm.bean.BpmFilterMatcher;

public interface BpmFilterMatcherService extends BaseService<BpmFilterMatcher>{

	public List<BpmFilterMatcher>findByFilterKey(String key);
	
	public void deleteByFilterKey(String key);
}
