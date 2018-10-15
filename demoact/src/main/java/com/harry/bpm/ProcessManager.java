package com.harry.bpm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;

import com.harry.ActivitiApp;
import com.harry.bpm.bean.BpmDimension;
import com.harry.bpm.bean.BpmProcdef;
import com.harry.bpm.exception.BpmException;
import com.harry.bpm.exception.BpmMessage;
import com.harry.bpm.jpa.service.BpmDimensionService;
import com.harry.bpm.jpa.service.BpmProcdefService;
import com.harry.bpm.registry.ActivitiRegistry;
import com.harry.bpm.util.Constant;

public class ProcessManager {

	private ProcessEngine processEngine;

	private RuntimeService runtimeService;

	private RepositoryService repositoryService;

	public ProcessManager() {
		processEngine = ActivitiApp.getInstance().initProcessEngine();
		runtimeService = processEngine.getRuntimeService();
		repositoryService = processEngine.getRepositoryService();
	}

	/**
	 * 删除流程实例
	 * 
	 * @param processInstanceId
	 * @param deleteReason
	 * @return
	 */
	public void deleteProcessInstance(String processInstanceId, String deleteReason) {
		try {
			processEngine.getRuntimeService().deleteProcessInstance(processInstanceId, deleteReason);
		} catch (ActivitiObjectNotFoundException e) {
			throw new BpmException(BpmMessage.E0001, e);
		}
	}

	/**
	 * 启动流程实例
	 * 
	 * @param fotypermKey
	 *            流程类型
	 * @param businessKey
	 *            业务编号
	 * @param description
	 *            业务描述
	 * @param variables
	 *            流程启动参数
	 * @param activitiRegistry
	 *            注册事件
	 * @return 返回流程实例id,方便业务动态扩展
	 */
	public String startProcessInstance(String type, String businessKey, String description,
			String userId, Map<String, Object> variables, ActivitiRegistry activitiRegistry) {
		if (null == variables) {
			variables = new HashMap<String, Object>();
		}
		variables.put(Constant.ACT_ACTION, activitiRegistry);
		//查询表单所涉及的流程维度
		BpmDimensionService dmsService = ActivitiApp.getInstance().initBpmDimensionService();
		List<BpmDimension> fdms = dmsService.findByProcType(type);
		Map<String, Object> vars=null;
		if (fdms!=null&&fdms.size()>0) {
			vars=new HashMap<String, Object>();
			for (int i = 0; i < fdms.size(); i++) {
				String key = fdms.get(i).getKey();
				vars.put(key, variables.get(key));
			}
		}
		//根据条件查询流程定义ID
		BpmProcdefService procdefService = ActivitiApp.getInstance().initBpmProcdefService();
		BpmProcdef procdef = procdefService.findDefinitionIdByMatcher(type, vars);
		if (null == procdef) {
			throw new BpmException(BpmMessage.E0008);
		}
		variables.put(Constant.ACT_PROC_DEFID, procdef.getKey());
		//检查是否存在流程模板
		if (procdef.getDeploymentKey()==null) {
			throw new BpmException(BpmMessage.E0009, type);
		}
		//启动流程
		Authentication.setAuthenticatedUserId(userId);
		ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKeyAndTenantId(procdef.getDeploymentKey(), businessKey,
				variables,type);

		runtimeService.setProcessInstanceName(pi.getId(), description);

		return pi.getId();
	}

	public ProcessInstance findSingleProcess(String processInstanceId) {
		boolean execute = false;
		ProcessInstance pi = null;
		ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery();
		if (null != processInstanceId) {
			query = query.processInstanceId(processInstanceId);
			execute = true;
		}
		if (execute) {
			pi = query.singleResult();
		}
		return pi;
	}

	public List<ProcessInstance> findListProcess(String processDefinitionId, String tenantId,String businessKey,
			String processInstanceId) {
		List<ProcessInstance> list = null;
		boolean execute = false;
		ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery();
		
		if (null != processDefinitionId) {
			query = query.processDefinitionId(processDefinitionId);
			execute = true;
		}
		if (null != tenantId) {
			query = query.processInstanceTenantId(tenantId);
			execute = true;
		}
		if (null != businessKey) {
			query = query.processInstanceBusinessKey(businessKey);
			execute = true;
		}
		if (null != processInstanceId) {
			query = query.processInstanceId(processInstanceId);
			execute = true;
		}
		if (execute) {
			list = query.orderByProcessInstanceId().desc().list();
		}

		return list;
	}

	public Long countProcess(String processDefinitionId, String tenantId, String businessKey) {
		ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery();
		if (null != processDefinitionId) {
			query = query.processDefinitionId(processDefinitionId);
		}
		if (null != tenantId) {
			query = query.processInstanceTenantId(processDefinitionId);
		}
		if (null != businessKey) {
			query = query.processInstanceBusinessKey(businessKey);
		}
		return query.count();

	}

	/**
	 * 根据流程实例查看流程图
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public List<Map<String, Object>> findCoording(String processInstanceId) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		ProcessInstance pi = runtimeService.createProcessInstanceQuery()// 创建流程实例查询
				.processInstanceId(processInstanceId)// 使用流程实例ID查询
				.singleResult();
		// 获取流程定义的ID
		String processDefinitionId = pi.getProcessDefinitionId();

		// 获取流程定义的实体对象（对应.bpmn文件中的数据）
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(processDefinitionId);

		// 查询流程实例下面的执行实例
		List<ExecutionEntity> executions = this.findExecution(pi.getProcessInstanceId(), null);

		Map<String, Object> map;
		for (int i = 0; i < executions.size(); i++) {
			ExecutionEntity eEntity = executions.get(i);
			// 获取当前活动的ID
			String activityId = eEntity.getActivityId();
			// 获取当前活动对象
			ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
			// 存放坐标
			map = new HashMap<String, Object>();
			map.put("x", activityImpl.getX());
			map.put("y", activityImpl.getY());
			map.put("width", activityImpl.getWidth());
			map.put("height", activityImpl.getHeight());
			map.put("key", eEntity.getId());
			map.put("activityId", activityId);
			result.add(map);
		}
		return result;
	}

	/**
	 * 查询流程实例下活动的Activity
	 * 
	 * @param pi
	 * @return
	 */
	public List<ActivityImpl> findActivity(ProcessInstance pi) {
		List<ActivityImpl> result = new ArrayList<ActivityImpl>();
		// 获取流程定义的ID
		String processDefinitionId = pi.getProcessDefinitionId();

		// 获取流程定义的实体对象（对应.bpmn文件中的数据）
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(processDefinitionId);

		// 查询流程实例下面的执行实例
		List<ExecutionEntity> executions = this.findExecution(pi.getProcessInstanceId(), null);
		for (int i = 0; i < executions.size(); i++) {
			ExecutionEntity eEntity = executions.get(i);
			// 获取当前活动的ID
			String activityId = eEntity.getActivityId();
			// 获取当前活动对象
			result.add(processDefinitionEntity.findActivity(activityId));
		}
		return result;
	}

	public List<ActivityImpl> findActivity(String processInstanceId) {
		List<ActivityImpl> result = new ArrayList<ActivityImpl>();
		// 创建流程实例查询
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();
		// 获取流程定义的ID
		String processDefinitionId = pi.getProcessDefinitionId();

		// 获取流程定义的实体对象（对应.bpmn文件中的数据）
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(processDefinitionId);

		// 查询流程实例下面的执行实例
		List<ExecutionEntity> executions = this.findExecution(pi.getProcessInstanceId(), null);
		for (int i = 0; i < executions.size(); i++) {
			ExecutionEntity eEntity = executions.get(i);
			// 获取当前活动的ID
			String activityId = eEntity.getActivityId();
			// 获取当前活动对象
			result.add(processDefinitionEntity.findActivity(activityId));
		}
		return result;
	}

	/**
	 * 查询正在执行的Execution
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public List<ExecutionEntity> findExecution(String processInstanceId, String executionId) {
		List<ExecutionEntity> result = new ArrayList<ExecutionEntity>();

		ExecutionQuery query = runtimeService.createExecutionQuery();

		if (null != processInstanceId) {
			query = query.processInstanceId(processInstanceId);
		}
		if (null != executionId) {
			query = query.executionId(executionId);
		}

		List<Execution> list = query.list();

		for (int i = 0; i < list.size(); i++) {
			ExecutionEntity execution = (ExecutionEntity) list.get(i);
			if (execution.isActive()) {
				result.add(execution);
			}
		}
		return result;
	}

	/**
	 * 获取流程中的自定义变量
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public Map<String, Object> getVariables(String processInstanceId) {
		Map<String, Object> vs = runtimeService.getVariables(processInstanceId);
		Map<String, Object> variables = new HashMap<String, Object>();
		if (null != vs) {
			Set<String> keys = vs.keySet();
			for (String key : keys) {
				if (!key.startsWith("ACT_")) {
					variables.put(key, vs.get(key));
				}
			}
		}
		return variables;
	}

}
