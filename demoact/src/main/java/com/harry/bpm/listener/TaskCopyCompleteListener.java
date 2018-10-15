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
 * 抄送任务完成
 * @author harry
 *
 */
public class TaskCopyCompleteListener implements TaskListener {

	private static final long serialVersionUID = 1L;
	
	public void notify(DelegateTask delegateTask) {

		CusTaskinst taskinst = new CusTaskinst(delegateTask.getProcessInstanceId(), delegateTask.getExecutionId(),
				delegateTask.getId(), TaskResult.COPY_READ.getStatus());

		ActivitiApp.getInstance().initCusTaskinstService().save(taskinst);

		ActivitiRegistry registry = (ActivitiRegistry)delegateTask.getVariable(Constant.ACT_ACTION);
		if (null!=registry) {
			TaskAction action = registry.getCopyTaskEnd();
			if (null!=action) {
				action.notify(delegateTask);
			}
		}
	}

}
