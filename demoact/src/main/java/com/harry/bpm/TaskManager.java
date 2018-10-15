package com.harry.bpm;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import com.harry.bpm.enums.TaskResult;
import com.harry.bpm.exception.BpmException;
import com.harry.bpm.exception.BpmMessage;
import com.harry.bpm.util.Constant;
import com.harry.bpm.util.TimeHelpe;

public class TaskManager {
	private ProcessEngine processEngine;

	private TaskService taskService;

	public TaskManager() {
		processEngine = ProcessEngines.getDefaultProcessEngine();
		// 正在执行的任务管理相关的Service
		taskService = processEngine.getTaskService();
	}

	/**
	 * 查询当前人的所有任务
	 * 
	 * @param assignee
	 * @return
	 */
	public List<Task> findTask(String assignee,String tenantId) {
		List<Task> list = taskService.createTaskQuery()
				.taskCandidateOrAssigned(assignee)
				.taskTenantId(tenantId)
				.orderByTaskCreateTime().desc().list();
		return list;
	}

	/**
	 * 查询当前人认领过来的任务
	 * 
	 * @param assignee
	 * @return
	 */
	public List<Task> findAssigneeTask(String assignee,String tenantId) {
		List<Task> list = taskService.createTaskQuery()
				.taskAssignee(assignee)
				.taskTenantId(tenantId)
				.orderByTaskCreateTime().desc()
				.list();
		return list;
	}

	/**
	 * 查询当前人候选任务
	 * 
	 * @param assignee
	 * @return
	 */
	public List<Task> findCandidateTask(String assignee,String tenantId) {
		List<Task> list = taskService.createTaskQuery()
				.taskCandidateUser(assignee)
				.taskTenantId(tenantId)
				.orderByTaskCreateTime().desc()
				.list();
		return list;
	}

	/**
	 * 查询单条任务实例
	 * 
	 * @param taskId
	 * @param executionId
	 * @return
	 */
	public Task findSingleTask(String taskId, String executionId) {
		if (null == taskId && null == executionId) {
			return null;
		}
		TaskQuery query = taskService.createTaskQuery();
		if (null != taskId) {
			query = query.taskId(taskId);
		}
		if (null != executionId) {
			query = query.executionId(executionId);
		}
		Task task = query.singleResult();
		return task;
	}

	public List<Comment> findTaskComments(String taskId,String type) {
		List<Comment> list = taskService.getTaskComments(taskId);
		return list;
	}
	
	public List<Comment> findProcessComments(String processInstanceId) {
		List<Comment> list = taskService.getProcessInstanceComments(processInstanceId);
		return list;
	}
	
	/**
	 * 根据类型查询当前账号已受理的总单据量
	 * @param assignee
	 * @param tenantId
	 * @return
	 */
	public long countAssigneeTask(String assignee,String tenantId) {
		return taskService.createTaskQuery()
				.taskAssignee(assignee)
				.taskTenantId(tenantId)
				.count();
	}
	
	/**
	 * 根据类型查询当前账号待受理的总单据量
	 * @param assignee
	 * @param tenantId
	 * @return
	 */
	public long countCandidateTask(String assignee,String tenantId) {
		return taskService.createTaskQuery()
				.taskCandidateUser(assignee)
				.taskTenantId(tenantId)
				.count();
	}
	
	public long countTask(String assignee,String tenantId) {
		return taskService.createTaskQuery()
				.taskCandidateOrAssigned(assignee)
				.taskTenantId(tenantId)
				.count();
	}
	/**
	 * 根据类型查询当前账号的截止到上月的总单据量
	 * @param assignee
	 * @param tenantId
	 * @return
	 */
	public long countTaskMonthBefore(String assignee,String tenantId) {
		return taskService.createTaskQuery()
				.taskCandidateOrAssigned(assignee)
				.taskTenantId(tenantId)
				.taskCreatedBefore(TimeHelpe.monthStart())
				.count();
	}

	/**
	 * 执行任务
	 * 
	 * @param taskId
	 * @param result
	 */
	public void completeTask(String processInstanceId, String taskId, TaskResult result, String commentMsg,
			String userId, Map<String, Object> variables) {
		try {
			// 审批意见
			Authentication.setAuthenticatedUserId(userId);
			taskService.addComment(taskId, processInstanceId, result.getStatus(), commentMsg);

			if (null == variables) {
				variables = new HashMap<String, Object>();
			}
			variables.put(Constant.ACT_TASK_RESULT, result);
			try {
				taskService.claim(taskId, userId);
			} catch (ActivitiTaskAlreadyClaimedException e) {
			}
			taskService.complete(taskId, variables);
		} catch (ActivitiObjectNotFoundException e) {
			throw new BpmException(BpmMessage.E0004, e);
		}

	}

	/**
	 * 非任务节点,推动流程继续执行
	 * 
	 * @param processInstanceId
	 * @param executeId
	 * @return
	 */
	public void completeSignal(String executionId, Map<String, Object> processVariables) {
		try {
			if (null != processVariables) {
				processEngine.getRuntimeService().signal(executionId, processVariables);
			} else {
				processEngine.getRuntimeService().signal(executionId);
			}
		} catch (ActivitiObjectNotFoundException e) {
			throw new BpmException(BpmMessage.E0005, e);
		}
	}

	public void completeSignal(String processInstanceId, String activityId, Map<String, Object> processVariables) {
		try {
			Execution execution = processEngine.getRuntimeService().createExecutionQuery()// 创建执行对象查询
					.processInstanceId(processInstanceId)// 使用流程实例ID查询
					.activityId(activityId)// 当前活动的id，对应receiveTask.bpmn文件中的活动节点id的属性值
					.singleResult();
			if (null != processVariables) {
				processEngine.getRuntimeService().signal(execution.getId(), processVariables);
			} else {
				processEngine.getRuntimeService().signal(execution.getId());
			}
		} catch (ActivitiObjectNotFoundException e) {
			throw new BpmException(BpmMessage.E0005, e);
		}
	}

	/**
	 * 指定任务添加组成员
	 * 
	 * @param taskId
	 * @param userId
	 */
	public void addGroupUser(String taskId, String userId) {
		try {
			taskService.addCandidateUser(taskId, userId);
		} catch (ActivitiObjectNotFoundException e) {
			throw new BpmException(BpmMessage.E0006, e);
		}
	}

	/**
	 * 指定任务删除组成员
	 * 
	 * @param taskId
	 * @param userId
	 */
	public void deleteGroupUser(String taskId, String userId) {
		try {
			taskService.deleteCandidateUser(taskId, userId);
		} catch (ActivitiObjectNotFoundException e) {
			throw new BpmException(BpmMessage.E0006, e);
		}
	}

	/**
	 * 任务指定审批人认领
	 * 
	 * @param taskId
	 * @param userId
	 */
	public void claimTask(String taskId, String userId) {
		try {
			taskService.claim(taskId, userId);
		} catch (ActivitiObjectNotFoundException e) {
			throw new BpmException(BpmMessage.E0006, e);
		}
	}

	/**
	 * 任务撤销认领
	 * 
	 * @param taskId
	 */
	public void unclaimTask(String taskId) {
		try {
			taskService.unclaim(taskId);
		} catch (ActivitiObjectNotFoundException e) {
			throw new BpmException(BpmMessage.E0006, e);
		}
	}

	/**
	 * 任务指定受理人
	 * 
	 * @param taskId
	 * @param userId
	 * @return
	 */
	public void assigneeTask(String taskId, String userId) {
		try {
			taskService.setAssignee(taskId, userId);
		} catch (ActivitiObjectNotFoundException e) {
			throw new BpmException(BpmMessage.E0006, e);
		}
	}

	/**
	 * 转移任务的拥有者
	 * 
	 * @param taskId
	 * @param userId
	 * @return
	 */
	public void ownerTask(String taskId, String userId, boolean withPending) {
		try {
			taskService.setOwner(taskId, userId);
			if (withPending) {
				Task task = this.findSingleTask(taskId, null);
				task.setDelegationState(DelegationState.PENDING);
			}
		} catch (ActivitiObjectNotFoundException e) {
			throw new BpmException(BpmMessage.E0006, e);
		}
	}

	/**
	 * 标记受让人已完成此任务, 并且可以将其发送回所有者
	 * 
	 * @param taskId
	 * @param userId
	 * @param variables
	 * @return
	 */
	public void resolveTask(String taskId, String userId, Map<String, Object> variables) {
		try {
			if (null == variables) {
				taskService.resolveTask(taskId);
			} else {
				taskService.resolveTask(taskId, variables);
			}
		} catch (ActivitiObjectNotFoundException e) {
			throw new BpmException(BpmMessage.E0006, e);
		}
	}

	/**
	 * 删除流程中的上下文属性
	 * 
	 * @param taskId
	 * @param variableName
	 * @return
	 */
	public void removeVariable(String taskId, String variableName) {
		try {
			taskService.removeVariable(taskId, variableName);
		} catch (ActivitiObjectNotFoundException e) {
			throw new BpmException(BpmMessage.E0006, e);
		}
	}

	/**
	 * 上传附件
	 * 
	 * @param processInstanceId
	 * @param taskId
	 * @param attachmentType
	 * @param attachmentName
	 * @param attachmentDescription
	 * @param content
	 *            输入流
	 * @return
	 */
	public Attachment addAttachment(String processInstanceId, String taskId, String userId, String attachmentType,
			String attachmentName, String attachmentDescription, InputStream content) {
		Authentication.setAuthenticatedUserId(userId);
		Attachment attachment = taskService.createAttachment(attachmentType, taskId, processInstanceId, attachmentName,
				attachmentDescription, content);
		return attachment;
	}

	/**
	 * 上传附件
	 * 
	 * @param processInstanceId
	 * @param taskId
	 * @param attachmentType
	 * @param attachmentName
	 * @param attachmentDescription
	 * @param url
	 *            外部链接
	 * @return
	 */
	public Attachment addAttachment(String processInstanceId, String taskId, String userId, String attachmentType,
			String attachmentName, String attachmentDescription, String url) {
		Authentication.setAuthenticatedUserId(userId);
		Attachment attachment = taskService.createAttachment(attachmentType, taskId, processInstanceId, attachmentName,
				attachmentDescription, url);
		return attachment;
	}

	/**
	 * 删除附件
	 * 
	 * @param attachmentId
	 * @return
	 */
	public void removeAttachment(String attachmentId) {
		try {
			taskService.deleteAttachment(attachmentId);
		} catch (ActivitiObjectNotFoundException e) {
			throw new BpmException(BpmMessage.E0007, e);
		}
	}

	/**
	 * 取流程附件
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public List<Attachment> getProcessInstanceAttachments(String processInstanceId) {
		List<Attachment> attachments = taskService.getProcessInstanceAttachments(processInstanceId);
		return attachments;
	}

	/**
	 * 取任务附件
	 * 
	 * @param taskId
	 * @return
	 */
	public List<Attachment> getTaskAttachments(String taskId) {
		List<Attachment> attachments = taskService.getTaskAttachments(taskId);
		return attachments;
	}
	/**
	 * 获取单条附件信息
	 * @param attachmentId
	 * @return
	 */
	public Attachment getAttachment(String attachmentId) {
		Attachment attachment = taskService.getAttachment(attachmentId);
		return attachment;
	}
	/**
	 * 获取附件内容
	 * @param attachmentId
	 * @return
	 */
	public InputStream getAttachmentContent(String attachmentId) {
		InputStream attachmentContent = taskService.getAttachmentContent(attachmentId);
		return attachmentContent;
	}
}
