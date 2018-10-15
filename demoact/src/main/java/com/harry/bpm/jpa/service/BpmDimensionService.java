package com.harry.bpm.jpa.service;

import java.util.List;

import com.harry.bpm.bean.BpmDimension;

public interface BpmDimensionService extends BaseService<BpmDimension>{

	List<BpmDimension> findByProcType(String procType);
	
}
