package com.harry.bpm.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.harry.ActivitiApp;
import com.harry.bpm.bean.BpmTask;
import com.harry.bpm.client.ExecutionAction;
import com.harry.bpm.registry.ActivitiRegistry;
import com.harry.bpm.util.Constant;
/**
 * 监听:
 * 抄送任务数计算
 * @author harry
 *
 */
public class ExecutionCopyListener implements ExecutionListener {

	private static final long serialVersionUID = 1L;
	
	public void notify(DelegateExecution execution) throws Exception {
		
		Map<String, Object> variables = execution.getVariables();
		
		List<String> copyList = (List<String>)variables.get(Constant.ACT_COPY_ROLES);
		if (null==copyList) {
			//没有查询到抄送岗,系统结束抄送
			execution.getEngineServices().getRuntimeService().signal(execution.getId());
		}
		
		
		ActivitiRegistry registry = (ActivitiRegistry) execution.getVariable(Constant.ACT_ACTION);
		if (null != registry) {
			ExecutionAction action = registry.getExecutionCopy();
			if (null != action) {
				action.notify(execution);
			}
		}
	}

}
