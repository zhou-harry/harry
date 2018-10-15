package com.harry.bpm.jpa.service;

import java.util.List;

import com.harry.bpm.bean.BpmProctype;

public interface BpmProctypeService extends BaseService<BpmProctype>{

	/**
	 * 根据父类型查询流程类型
	 * @param parent 父流程类型编号
	 * @return
	 */
	public List<BpmProctype> findProctypeByParent(String parent);
	
	/**
	 * 根据用户查询其已经挂载模板的流程类型
	 * @param userid
	 * @return
	 */
	public List<BpmProctype> findDeploymentByUser(String userid);
	
	/**
	 * 根据流程类型查询
	 * @param type
	 * @return
	 */
	public BpmProctype findByType(String type);
	
}
