package com.harry.bpm;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.harry.ActivitiApp;
import com.harry.bpm.bean.BpmDimension;
import com.harry.bpm.bean.BpmFilter;
import com.harry.bpm.bean.BpmFilterMatcher;
import com.harry.bpm.bean.BpmMatcher;
import com.harry.bpm.bean.BpmOwner;
import com.harry.bpm.bean.BpmProcdef;
import com.harry.bpm.bean.BpmProcdms;
import com.harry.bpm.bean.BpmProctype;
import com.harry.bpm.bean.BpmRole;
import com.harry.bpm.bean.BpmTask;
import com.harry.bpm.bean.BpmTaskRole;
import com.harry.bpm.exception.BpmException;
import com.harry.bpm.exception.BpmMessage;
import com.harry.bpm.jpa.repository.BpmRoleRepository;
import com.harry.bpm.jpa.service.BpmDimensionService;
import com.harry.bpm.jpa.service.BpmFilterMatcherService;
import com.harry.bpm.jpa.service.BpmFilterService;
import com.harry.bpm.jpa.service.BpmMatcherService;
import com.harry.bpm.jpa.service.BpmOwnerService;
import com.harry.bpm.jpa.service.BpmProcdefService;
import com.harry.bpm.jpa.service.BpmProcdmsService;
import com.harry.bpm.jpa.service.BpmProctypeService;
import com.harry.bpm.jpa.service.BpmRoleService;
import com.harry.bpm.jpa.service.BpmTaskRoleService;
import com.harry.bpm.jpa.service.BpmTaskService;
import com.harry.bpm.util.Constant;

public class BpmManager {

	private BpmProctypeService proctypeService;
	private BpmDimensionService dimensionService;
	private BpmProcdmsService procdmsService;
	private BpmMatcherService matcherService;
	private BpmProcdefService procdefService;
	private BpmFilterService filterService;
	private BpmFilterMatcherService filterMatcherService;
	private BpmRoleService roleService;
	private BpmOwnerService ownerService;
	private BpmTaskService taskService;
	private BpmTaskRoleService taskRoleService;
	
	public BpmTaskRoleService getTaskRoleService() {
		if (null == taskRoleService) {
			taskRoleService = ActivitiApp.getInstance().initBpmTaskRoleService();
		}
		return taskRoleService;
	}
	
	public BpmTaskService getTaskService() {
		if (null == taskService) {
			taskService = ActivitiApp.getInstance().initBpmTaskService();
		}
		return taskService;
	}
	
	public BpmOwnerService getOwnerService() {
		if (null == ownerService) {
			ownerService = ActivitiApp.getInstance().initBpmOwnerService();
		}
		return ownerService;
	}
	
	public BpmRoleService getRoleService() {
		if (null == roleService) {
			roleService = ActivitiApp.getInstance().initBpmRoleService();
		}
		return roleService;
	}
	
	public BpmFilterMatcherService getFilterMatcherService() {
		if (null == filterMatcherService) {
			filterMatcherService = ActivitiApp.getInstance().initBpmFilterMatcherService();
		}
		return filterMatcherService;
	}
	
	public BpmFilterService getFilterService() {
		if (null == filterService) {
			filterService = ActivitiApp.getInstance().initBpmFilterService();
		}
		return filterService;
	}
	
	public BpmProcdefService getProcdefService() {
		if (null == procdefService) {
			procdefService = ActivitiApp.getInstance().initBpmProcdefService();
		}
		return procdefService;
	}

	public BpmMatcherService getMatcherService() {
		if (null == matcherService) {
			matcherService = ActivitiApp.getInstance().initBpmMatcherService();
		}
		return matcherService;
	}
	
	public BpmProctypeService getProctypeService() {
		if (null == proctypeService) {
			proctypeService = ActivitiApp.getInstance().initBpmProctypeService();
		}
		return proctypeService;
	}

	public BpmDimensionService getDimensionService() {
		if (null == dimensionService) {
			dimensionService = ActivitiApp.getInstance().initBpmDimensionService();
		}
		return dimensionService;
	}

	public BpmProcdmsService getProcdmsService() {
		if (null == procdmsService) {
			procdmsService = ActivitiApp.getInstance().initBpmProcdmsService();
		}
		return procdmsService;
	}
	

	public List<BpmProctype> findProctypeByParent(String parent, boolean withTree) {
		if (null == parent) {
			parent = Constant.BPM_PROC_TYPE_ROOT;
		}
		List<BpmProctype> list = getProctypeService().findProctypeByParent(parent);
		if (withTree) {
			for (int i = 0; i < list.size(); i++) {
				BpmProctype type = list.get(i);
				type.setItems(this.findProctypeByParent(type.getType(), withTree));
			}
		}
		return list;
	}

	/**
	 * 查询流程维度
	 * 
	 * @return
	 */
	public List<BpmDimension> findDimension() {
		return getDimensionService().findAll(BpmDimension.class);
	}

	/**
	 * 根据流程类型查询维度
	 * 
	 * @param procType
	 * @return
	 */
	public List<BpmDimension> findDimensionByProcType(String procType) {
		return getDimensionService().findByProcType(procType);
	}

	/**
	 * 流程匹配器
	 * 查询
	 * @param procType
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> findProcMatcher(String formKey) {
		return getMatcherService().findProcMatcher(formKey);
	}
	
	/**
	 * 流程定义
	 * 查询
	 * @return
	 * @throws Exception
	 */
	public List<BpmProcdef> findProcdef() throws Exception {
		return getProcdefService().findAll(BpmProcdef.class);
	}

	/**
	 * 规则过滤器
	 * 查询所有
	 * @return
	 */
	public List<BpmFilter>findFilter(){
		return this.getFilterService().findAll(BpmFilter.class);
	}

	/**
	 * 规则过滤器
	 * 根据规则编号查询
	 * @param key
	 * @return
	 */
	public Map<String, Object>findByKey(String key){
		return this.getFilterService().findByKey(key);
	}
	/**
	 * 规则过滤器
	 * 递归查询当前规则可以relation的规则
	 * @param key
	 * @return
	 */
	public List<BpmFilter>findForRelation(String key){
		return this.getFilterService().findForRelation(key);
	}
	/**
	 * 规则过滤器(自定义)
	 * 根据规则编号查询
	 * @param key
	 * @return
	 */
	public List<BpmFilterMatcher> findByMatcherFilterKey(String key){
		return this.getFilterMatcherService().findByFilterKey(key);
	}
	/**
	 * 流程角色
	 * 查询
	 * @return
	 */
	public List<BpmRole>findRoles(){
		return this.getRoleService().findAll(BpmRole.class);
	}
	
	/**
	 * 流程审批人
	 * 查询
	 * @param roleid
	 * @return
	 */
	public List<BpmOwner>findOwnersByRole(String roleid){
		return this.getOwnerService().findOwnerByRole(roleid);
	}
	
	/**
	 * 根据流程定义查询任务节点
	 * @param definitionId
	 * @return
	 */
	public List<BpmTask>findTasks(String definitionId){
		List<BpmTask> list = this.getTaskService().findTasks(definitionId);
		if (null!=list) {
			for (int i = 0; i < list.size(); i++) {
				BpmTask t = list.get(i);
				
				t.setFilterName(this.getFilterService().findNameByKey(t.getFilterId()));
			}
		}
		return list;
	}
	
	/**
	 * 审批节点角色
	 * 查询
	 * @param taskId
	 * @return
	 */
	public List<BpmTaskRole>findRoleByTask(String taskId){
		return this.getTaskRoleService().findByTask(taskId);
	}
	
	/**
	 * 根据用户查询其已经挂载模板的流程类型
	 * @param userid
	 * @return
	 */
	public List<BpmProctype> findCategoryByUser(String userid) {
		return this.getProctypeService().findDeploymentByUser(userid);
	}
	
	/**
	 * 流程定义任务节点数
	 * 查询
	 * @param definitionId
	 * @return
	 */
	public Integer countTasks(String definitionId){
		return this.getTaskService().countTasks(definitionId);
	}
	/**
	 * 流程审批人数
	 * 查询
	 * @param roleid
	 * @return
	 */
	public Integer countOwnerByRole(String roleid) {
		return this.getOwnerService().countOwnerByRole(roleid);
	}
	

	/**
	 * 保存流程维度
	 * 
	 * @param t
	 */
	public void saveDimension(BpmDimension t) {
		getDimensionService().save(t);
	}

	/**
	 * 根据流程类型保存维度信息
	 * 
	 * @param list
	 */
	public void saveDimensionWithProcType(List<BpmProcdms> list) {
		getProcdmsService().save(list);
	}

	/**
	 * 流程匹配器
	 * 保存
	 * @param list
	 */
	public void saveProcMatcher(List<BpmMatcher> t) {
		getMatcherService().save(t);
	}

	/**
	 * 流程定义
	 * 保存
	 * @param t
	 */
	public void saveProcdef(BpmProcdef t) {
		getProcdefService().save(t);
	}
	
	/**
	 * 流程类型
	 * 保存
	 * @param t
	 */
	public void saveProctype(BpmProctype t) {
		getProctypeService().save(t);
	}


	/**
	 * 规则过滤器
	 * 保存
	 * @param t
	 */
	public void saveFilter(BpmFilter t) {
		this.getFilterService().save(t);
	}
	
	/**
	 * 规则过滤器(自定义)
	 * 保存
	 * @param ts
	 */
	public void saveFilterMatcher(List<BpmFilterMatcher>ts) {
		this.getFilterMatcherService().save(ts);
	}
	/**
	 * 规则过滤器(自定义)
	 * 保存
	 * @param t
	 */
	public void saveFilterMatcher(BpmFilterMatcher t) {
		this.getFilterMatcherService().save(t);
	}
	
	/**
	 * 流程角色
	 * 保存
	 * @param t
	 */
	public void saveRole(BpmRole t) {
		this.getRoleService().save(t);
	}
	
	/**
	 * 流程审批人
	 * 保存
	 * @param t
	 */
	public void saveOwner(BpmOwner t) {
		this.getOwnerService().save(t);
	}
	
	/**
	 * 流程审批节点
	 * 保存
	 * @param t
	 */
	public void saveTask(BpmTask t) {
		this.getTaskService().save(t);
	}
	
	/**
	 * 流程审批节点角色
	 * 保存
	 * @param t
	 */
	public void saveTaskRole(BpmTaskRole t) {
		this.getTaskRoleService().save(t);
	}

	/**
	 * 删除流程维度
	 * 
	 * @param t
	 */
	public void deleteDimension(BpmDimension t) {
		getDimensionService().delete(t);
	}

	/**
	 * 根据流程类型删除维度信息
	 * 
	 * @param t
	 */
	public void deleteDimensionByProcType(BpmProcdms t) {
		getProcdmsService().delete(t);
	}

	/**
	 * 流程匹配器
	 * 删除
	 * @param t
	 */
	public void deleteProcMatcher(BpmMatcher t) {
		getMatcherService().delete(t);
	}
	
	/**
	 * 流程定义
	 * 删除
	 * @param t
	 */
	public void deleteProcdef(BpmProcdef t) {
		getProcdefService().delete(t);
	}
	/**
	 * 规则过滤器
	 * 删除
	 * @param t
	 */
	public void deleteFilter(BpmFilter t) {
		this.getFilterService().delete(t);
	}
	/**
	 * 规则过滤器(自定义)
	 * 删除
	 * @param t
	 */
	public void deleteFilterMatcher(BpmFilterMatcher t) {
		this.getFilterMatcherService().delete(t);
	}
	
	/**
	 * 规则过滤器(自定义)
	 * 根据规则编号删除
	 * @param t
	 */
	public void deleteFilterMatcher(String key) {
		this.getFilterMatcherService().deleteByFilterKey(key);
	}
	/**
	 * 流程角色
	 * 删除
	 * @param t
	 */
	public void deleteRole(BpmRole t) {
		this.getRoleService().delete(t);
	}
	/**
	 * 流程审批人
	 * 删除
	 * @param t
	 */
	public void deleteOwner(BpmOwner t) {
		this.getOwnerService().delete(t);
	}
	/**
	 * 流程审批人
	 * 根据角色删除
	 * @param roleid
	 */
	public void deleteByRole(String roleid) {
		this.getOwnerService().deleteByRole(roleid);
	}
	/**
	 * 流程审批节点
	 * 删除
	 * @param t
	 */
	public void deleteTask(BpmTask t) {
		this.getTaskService().delete(t);
	}
	
	/**
	 * 流程审批节点角色
	 * 删除
	 * @param t
	 */
	public void deleteTaskRole(BpmTaskRole t) {
		this.getTaskRoleService().delete(t);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
