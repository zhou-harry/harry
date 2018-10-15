package com.harry.bpm.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.TaskServiceImpl;

import com.harry.ActivitiApp;
import com.harry.bpm.bean.CusTaskinst;
import com.harry.bpm.client.TaskAction;
import com.harry.bpm.enums.TaskResult;
import com.harry.bpm.registry.ActivitiRegistry;
import com.harry.bpm.service.DynaTaskService;
import com.harry.bpm.util.Constant;

/**
 * 监听:
 * 单签任务完成
 * 
 * @author harry
 *
 */
public class TaskSingleCompleteListener implements TaskListener {

	private static final long serialVersionUID = 1L;
	
	public void notify(DelegateTask delegateTask) {

		TaskResult result = (TaskResult) delegateTask.getVariable(Constant.ACT_TASK_RESULT);

		switch (result) {
		case REJECT:
			delegateTask.setVariable(Constant.ACT_AGREED_COUNT, -1);
			break;
		default:
			delegateTask.setVariable(Constant.ACT_AGREED_COUNT, 1);
			break;
		}
		CusTaskinst taskinst = new CusTaskinst(delegateTask.getProcessInstanceId(), delegateTask.getExecutionId(),
				delegateTask.getId(), result.getStatus());
		
		ActivitiApp.getInstance().initCusTaskinstService().save(taskinst);
		
		ActivitiRegistry registry = (ActivitiRegistry)delegateTask.getVariable(Constant.ACT_ACTION);
		if (null!=registry) {
			TaskAction action = registry.getSingleTaskEnd();
			if (null!=action) {
				action.notify(delegateTask);
			}
		}
	}

}
