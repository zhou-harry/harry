package com.harry.bpm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricVariableInstanceEntity;

import com.harry.ActivitiApp;
import com.harry.bpm.util.Constant;

public class HistoryManager {

	private ProcessEngine processEngine;

	private HistoryService historyService;

	private RuntimeService runtimeService;

	public HistoryManager() {
		processEngine = ActivitiApp.getInstance().initProcessEngine();
		historyService = processEngine.getHistoryService();
		runtimeService = processEngine.getRuntimeService();
	}

	/**
	 * 查询流程历史
	 * @param processDefinitionId 流程定义
	 * @param processInstanceId 流程实例
	 * @param businessKey 业务id
	 * @return
	 */
	public List<Map<String, Object>> findProcess(String processDefinitionId,String processInstanceId,String businessKey,String name) {
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		boolean execute = false;
		if (null!=processDefinitionId) {
			query=query.processDefinitionId(processDefinitionId);
			execute = true;
		}
		if (null!=processInstanceId) {
			query=query.processInstanceId(processInstanceId);
			execute = true;
		}
		if (null!=businessKey) {
			query=query.processInstanceBusinessKey(businessKey);
			execute = true;
		}
		if (null!=name) {
			query=query.processInstanceNameLikeIgnoreCase(name);
			execute = true;
		}
		if (execute) {
			List<HistoricProcessInstance> list = query.orderByProcessInstanceStartTime().desc().list();
			if (null != list) {
				for (int i = 0; i < list.size(); i++) {
					HistoricProcessInstanceEntity instance = (HistoricProcessInstanceEntity)list.get(i);
					
					result.add(historyInfo(instance));
				}
			}
		}
		return result;
	}
	/**
	 * 查询单条流程实例
	 * @param processInstanceId
	 * @return
	 */
	public Map<String, Object> findSingleProcess(String processInstanceId) {
		Map<String, Object> info =new HashMap<String, Object>();
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
		boolean execute = false;
		if (null!=processInstanceId) {
			query=query.processInstanceId(processInstanceId);
			execute = true;
		}
		if (execute) {
			HistoricProcessInstanceEntity instance = (HistoricProcessInstanceEntity)query.singleResult();
	
			info=historyInfo(instance);
		}
		return info;
	}

	/**
	 * 根据流程号查询
	 * @param processInstanceId
	 * @return
	 */
	public Map<String, Object> findProcessById(String processInstanceId) {
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId);
		
			List<HistoricProcessInstance> list = query.list();
			if (null != list) {
				for (int i = 0; i < list.size(); i++) {
					HistoricProcessInstanceEntity instance = (HistoricProcessInstanceEntity)list.get(i);
					
					return historyInfo(instance);
				}
			}
		return null;
	}
	/**
	 * 查询流程历史
	 * @param tenantId
	 * @return
	 */
	public List<Map<String, Object>> findProcessByType(String tenantId) {
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
				.processInstanceTenantId(tenantId)
				.orderByProcessInstanceStartTime().desc()
				.list();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if (null != list) {
			for (int i = 0; i < list.size(); i++) {
				HistoricProcessInstanceEntity instance = (HistoricProcessInstanceEntity)list.get(i);
				
				result.add(historyInfo(instance));
			}
		}
		return result;
	}
	/**
	 * 根据流程类型查询我的申请
	 * @param tenantId
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> findProcessByStarter(String tenantId,String userId) {
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
				.processInstanceTenantId(tenantId)
				.startedBy(userId)
				.orderByProcessInstanceStartTime().desc()
				.list();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if (null != list) {
			for (int i = 0; i < list.size(); i++) {
				HistoricProcessInstanceEntity instance = (HistoricProcessInstanceEntity)list.get(i);
				
				result.add(historyInfo(instance));
			}
		}
		return result;
	}
	/**
	 * 查询审批历史流程号
	 * @param tenantId
	 * @param owner
	 * @return
	 */
	public HashSet<String> findProcessIdByOwner(String tenantId,String owner){
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
				.taskTenantId(tenantId)
				.taskAssignee(owner).list();
		HashSet<String>ids=new HashSet<String>();
		for (int i = 0; i < list.size(); i++) {
			HistoricTaskInstance t = list.get(i);
			ids.add(t.getProcessInstanceId());
		}
		return ids;
	}

	/**
	 * 查询流程历史变量
	 * 
	 * @param processInstanceId
	 * @param variableName
	 * @return
	 */
	public List<HistoricVariableInstance> findVariables(String processInstanceId, String variableName) {
		boolean execute = false;
		List<HistoricVariableInstance> list = null;
		HistoricVariableInstanceQuery query = historyService.createHistoricVariableInstanceQuery();
		if (null != processInstanceId) {
			query = query.processInstanceId(processInstanceId);
			execute = true;
		}
		if (null != variableName) {
			query = query.variableName(variableName);
			execute = true;
		}
		if (execute) {
			list = query.list();
		}
		return list;
	}

	/**
	 * 精确查询流程变量
	 * 
	 * @param processInstanceId
	 * @param variableName
	 * @return
	 */
	public HistoricVariableInstance findVariable(String processInstanceId, String variableName) {
		HistoricVariableInstanceQuery query = historyService.createHistoricVariableInstanceQuery();
		if (null != processInstanceId && null != variableName) {
			return query.processInstanceId(processInstanceId).variableName(variableName).singleResult();
		}
		return null;
	}

	/**
	 * 查询流程变量
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public Map<String, Object> getVariables(String processInstanceId) {
		Map<String, Object> variables = new HashMap<String, Object>();

		List<HistoricVariableInstance> list = this.findVariables(processInstanceId, null);
		if (null != list) {
			for (int i = 0; i < list.size(); i++) {
				boolean inner = false;
				HistoricVariableInstance v = list.get(i);

				if (v.getVariableName().startsWith("ACT_")) {
					continue;
				}
				List<String> inners = Constant.ACT_INNER;
				for (int j = 0; j < inners.size(); j++) {
					if (inners.get(j).equals(v.getVariableName())) {
						inner = true;
						continue;
					}
				}
				if (inner) {
					continue;
				}
				variables.put(v.getVariableName(), v.getValue());
			}
		}
		return variables;
	}

	public long sizeProcess(String processDefinitionId, String processInstanceId, String businessKey) {
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();

		if (null != processDefinitionId) {
			query = query.processDefinitionId(processDefinitionId);
		}
		if (null != processInstanceId) {
			query = query.processInstanceId(processInstanceId);
		}
		if (null != businessKey) {
			query = query.processInstanceBusinessKey(businessKey);
		}
		return query.count();

	}
	
	public long sizeProcessByType(String tenantId) {
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery().processInstanceTenantId(tenantId);
		return query.count();
	}

	/**
	 * 当前账号的审批历史数量
	 * @param tenantId
	 * @param owner
	 * @return
	 */
	public long sizeProcessByOwner(String tenantId,String owner){
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
				.taskTenantId(tenantId)
				.taskAssignee(owner).list();
		HashSet<String>id=new HashSet<String>();
		for (int i = 0; i < list.size(); i++) {
			HistoricTaskInstance t = list.get(i);
			id.add(t.getProcessInstanceId());
		}
		return id.size();
	}
	/**
	 * 当前账号的申请历史数量
	 * @param tenantId
	 * @param owner
	 * @return
	 */
	public long sizeProcessByStarter(String tenantId,String owner){
		return historyService.createHistoricProcessInstanceQuery()
		.processInstanceTenantId(tenantId)
		.startedBy(owner)
		.count();
	}

	/**
	 * 根据流程实例查询历史任务
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public List<HistoricTaskInstance> findTask(String processInstanceId) {
		HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery();
		if (null != processInstanceId) {
			query = query.processInstanceId(processInstanceId);
		}
		List<HistoricTaskInstance> list = query.orderByTaskCreateTime().asc().list();
		return list;
	}

	/**
	 * 查询历史活动
	 * 
	 * @param processInstanceId
	 */
	public List<HistoricActivityInstance> findActiviti(String processInstanceId) {
		HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery();

		if (null != processInstanceId) {
			query = query.processInstanceId(processInstanceId);
		}

		List<HistoricActivityInstance> list = query.orderByHistoricActivityInstanceStartTime().asc().list();
		return list;
	}
	
	private Map<String, Object> historyInfo(HistoricProcessInstanceEntity entity){
		Map<String, Object> info =new HashMap<String, Object>();
		
		HistoricVariableInstanceEntity ACT_PROC_DEFID = (HistoricVariableInstanceEntity)this.findVariable(entity.getProcessInstanceId(), Constant.ACT_PROC_DEFID);
		
		info.put("instanceId", entity.getProcessInstanceId());
		info.put("description", entity.getName());
		info.put("startTime", entity.getStartTime());
		info.put("businessKey", entity.getBusinessKey());
		info.put("startUserId", entity.getStartUserId());
		info.put("processKey", entity.getTenantId());
		info.put("deploymentId", entity.getDeploymentId());
		info.put("definitionKey", entity.getProcessDefinitionKey());
		info.put("definitionVersion", entity.getProcessDefinitionVersion());
		info.put("tenantId", entity.getTenantId());
		if (null!=ACT_PROC_DEFID) {
			info.put("bpmKey", ACT_PROC_DEFID.getCachedValue());
		}
		
		return info;
	}
}
