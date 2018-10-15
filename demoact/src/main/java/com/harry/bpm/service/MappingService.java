package com.harry.bpm.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.harry.ActivitiApp;
import com.harry.bpm.bean.BpmProcdef;
import com.harry.bpm.bean.BpmTask;
import com.harry.bpm.enums.TaskType;
import com.harry.bpm.jpa.service.BpmRoleService;
import com.harry.bpm.util.Constant;

/**
 * 任务映射服务 (计算下一步任务)
 * 
 * @author harry
 *
 */
public class MappingService implements JavaDelegate {

	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("动态任务映射服务启动...");
		Map<String, Object> variables = new HashMap<String, Object>();

		variables.put(Constant.ACT_AGREED_COUNT, 0);// 审批同意票数
		variables.put(Constant.ACT_CANDIDATE_LIST, null);

		String procDefid = (String) execution.getVariable(Constant.ACT_PROC_DEFID);
		Object taskCurrent = execution.getVariable(Constant.ACT_TASK_CURRENT_OBJECT);

		if (null == taskCurrent) {
			BpmProcdef definition = ActivitiApp.getInstance().initBpmProcdefService().findByKey(procDefid);
			if (null == definition) {
				variables.put(Constant.ACT_TASK_NEXT, "N");// 是否有下一步审批
				return;
			}
			this.nextTask(variables, procDefid, null);
		} else {
			BpmTask task = (BpmTask) taskCurrent;
			this.nextTask(variables, task.getDefinitionId(), task.getTaskId());
		}
		execution.setVariables(variables);

	}

	private void nextTask(Map<String, Object> variables, String definitionId, String taskId) {
		
		ActivitiApp activitiApp = ActivitiApp.getInstance();
		
		// 下一步审批任务信息
		BpmTask nextTask = activitiApp.initBpmTaskService().findNextTask(definitionId, taskId);
		variables.put(Constant.ACT_TASK_CURRENT_OBJECT, nextTask);
		if (null == nextTask) {
			variables.put(Constant.ACT_TASK_NEXT, "N");
		} else {
			variables.put(Constant.ACT_TASK_NEXT, "Y");
			variables.put(Constant.ACT_TASK_TIMER, "R1/PT"+nextTask.getDuration()+"S");//R3/PT20S
			variables.put(Constant.ACT_TASK_PENDING, nextTask.getPending()?"Y":"N");
			
			BpmRoleService roleService = activitiApp.initBpmRoleService();
			//会签
			if (nextTask.getType()==TaskType.MULTI.getKey().intValue()) {
				variables.put(Constant.ACT_TASK_MULTI, "Y");
				//查询会签岗位
				List<String> candidateList = roleService.findRoleIdByTask(nextTask.getDefinitionId(),
						nextTask.getTaskId());
				variables.put(Constant.ACT_CANDIDATE_LIST, candidateList);
			} else {// 单签
				variables.put(Constant.ACT_TASK_MULTI, "N");
				// 查询任务的审批人
				List<String> candidateList = activitiApp.initBpmOwnerService()
						.findOwnerIdByTask(nextTask.getDefinitionId(), nextTask.getTaskId());
				// 潜在审批人
				variables.put(Constant.ACT_CANDIDATE_LIST, candidateList);
				// 检查抄送
				List<String> copyRoles = roleService.findCopyRoleIdByTask(nextTask.getDefinitionId(),
						nextTask.getTaskId());
				variables.put(Constant.ACT_COPY_TYPE, copyRoles.size()>0 ? "Y" : "N");
				variables.put(Constant.ACT_COPY_ROLES, copyRoles);
			}

		}
	}
}
