package com.harry.bpm.jpa.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.harry.bpm.bean.BpmMatcher;

public interface BpmMatcherService extends BaseService<BpmMatcher>{

	public List<Map<String, Object>> findProcMatcher(String formKey);
	
	public List<Map<String, Object>> findTypeDms(String formKey,String procKey);
	
}
