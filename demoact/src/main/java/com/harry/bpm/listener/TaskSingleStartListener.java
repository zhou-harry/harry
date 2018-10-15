package com.harry.bpm.listener;

import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.harry.bpm.bean.BpmTask;
import com.harry.bpm.client.TaskAction;
import com.harry.bpm.registry.ActivitiRegistry;
import com.harry.bpm.util.Constant;

/**
 * 监听:
 * 单签任务启动
 * @author harry
 *
 */
public class TaskSingleStartListener implements TaskListener {

	private static final long serialVersionUID = 1L;

	public void notify(DelegateTask delegateTask) {
		
		Map<String, Object> variables = delegateTask.getVariables();
		
		// 如果候选人中只有一个人,直接指定为审批人
		List<String> list = (List<String>) variables.get(Constant.ACT_CANDIDATE_LIST);
		if (null==list) {
			delegateTask.setAssignee(Constant.ACT_APPR_ADMIN);
		}else if (list.size() == 1) {
			delegateTask.setAssignee(list.get(0));
		}else {
			delegateTask.addCandidateUsers(list);
		}
		
		BpmTask task = (BpmTask)variables.get(Constant.ACT_TASK_CURRENT_OBJECT);
		if (null!=task) {
			delegateTask.setName(task.getName());
			delegateTask.setFormKey(task.getTaskId());
			delegateTask.setDescription("【单签】");
		}
		
		ActivitiRegistry registry = (ActivitiRegistry)delegateTask.getVariable(Constant.ACT_ACTION);
		if (null!=registry) {
			TaskAction action = registry.getSingleTaskStart();
			if (null!=action) {
				action.notify(delegateTask);
			}
		}
	}

}
