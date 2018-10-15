package com.harry.bpm.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.harry.bpm.client.ExecutionAction;
import com.harry.bpm.registry.ActivitiRegistry;
import com.harry.bpm.util.Constant;

/**
 * 监听:
 * 无任务结束流程
 * @author harry
 *
 */
public class ExecutionNoTaskListener implements ExecutionListener {

	private static final long serialVersionUID = 1L;
	
	public void notify(DelegateExecution execution) throws Exception {

		ActivitiRegistry registry = (ActivitiRegistry) execution.getVariable(Constant.ACT_ACTION);
		if (null != registry) {
			ExecutionAction action = registry.getExecutionNoTask();
			if (null != action) {
				action.notify(execution);
			}
		}
	}

}
