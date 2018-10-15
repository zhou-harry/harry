package com.harry.bpm.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import com.harry.ActivitiApp;
import com.harry.bpm.bean.BpmTask;
import com.harry.bpm.bean.CusTaskinst;
import com.harry.bpm.client.ExecutionAction;
import com.harry.bpm.enums.TaskResult;
import com.harry.bpm.registry.ActivitiRegistry;
import com.harry.bpm.util.Constant;

/**
 * 监听:
 * 单签定时器
 * @author harry
 *
 */
public class ExecutionSingleJobListener implements ExecutionListener {

	private static final long serialVersionUID = 1L;
	
	public void notify(DelegateExecution execution) throws Exception {
		
		Task task = execution.getEngineServices().getTaskService().createTaskQuery().executionId(execution.getId())
				.singleResult();
		
		Map<String, Object> variables = execution.getVariables();
		
		if (null!=task) {//cancel activiti=true的情况下,此处才能拿到当前任务节点
			
			Object taskCurrent = execution.getVariable(Constant.ACT_TASK_CURRENT_OBJECT);
			if (null!=taskCurrent) {
				List<String>candidateList=new ArrayList<String>();
				candidateList.add(((BpmTask)taskCurrent).getStandby());
				variables.put(Constant.ACT_CANDIDATE_LIST, candidateList);
				execution.setVariables(variables);
			}
			
			execution.setVariable(Constant.ACT_TASK_RESULT, TaskResult.JOB_DELEGETE);
			
			CusTaskinst taskinst = new CusTaskinst(execution.getProcessInstanceId(), execution.getId(),
					task.getId(), TaskResult.JOB_DELEGETE.getStatus());
			ActivitiApp.getInstance().initCusTaskinstService().save(taskinst);
		}
		
		ActivitiRegistry registry = (ActivitiRegistry) execution.getVariable(Constant.ACT_ACTION);
		if (null != registry) {
			ExecutionAction action = registry.getExclusiveSingleJob();
			if (null != action) {
				action.notify(execution);
			}
		}
	}

}
