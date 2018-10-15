package com.harry.bpm.listener;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.IdentityLink;

import com.harry.ActivitiApp;
import com.harry.bpm.bean.BpmTask;
import com.harry.bpm.client.TaskAction;
import com.harry.bpm.registry.ActivitiRegistry;
import com.harry.bpm.util.Constant;

/**
 * 监听:
 * 会签任务启动
 * 
 * @author harry
 *
 */
public class TaskMultiStartListener implements TaskListener {

	private static final long serialVersionUID = 1L;
	
	public void notify(DelegateTask delegateTask) {

		ActivitiApp activitiApp = ActivitiApp.getInstance();
		Map<String, Object> variables = delegateTask.getVariables();
		// 岗位
		Set<IdentityLink> candidates = delegateTask.getCandidates();
		for (IdentityLink identityLink : candidates) {
			if (delegateTask.getId().equals(identityLink.getTaskId())) {
				String roleid = identityLink.getUserId();

				delegateTask.deleteCandidateUser(roleid);

				List<String> owners = activitiApp.initBpmOwnerService().findOwnerIdByRole(roleid);
				for (int i = 0; i < owners.size(); i++) {
					delegateTask.addCandidateUser(owners.get(i));
				}
				break;
			}
		}

		BpmTask task = (BpmTask) variables.get(Constant.ACT_TASK_CURRENT_OBJECT);
		if (null != task) {
			delegateTask.setName(task.getName());
			delegateTask.setFormKey(task.getTaskId());
			delegateTask.setDescription("【会签】");
		}
		
		ActivitiRegistry registry = (ActivitiRegistry)delegateTask.getVariable(Constant.ACT_ACTION);
		if (null!=registry) {
			TaskAction action = registry.getMultiTaskStart();
			if (null!=action) {
				action.notify(delegateTask);
			}
		}
	}

}
