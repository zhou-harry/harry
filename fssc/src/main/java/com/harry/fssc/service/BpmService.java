package com.harry.fssc.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harry.ActivitiApp;
import com.harry.bpm.BpmManager;
import com.harry.bpm.HistoryManager;
import com.harry.bpm.ProcessManager;
import com.harry.bpm.TaskManager;
import com.harry.bpm.bean.BpmDimension;
import com.harry.bpm.bean.BpmFilter;
import com.harry.bpm.bean.BpmFilterMatcher;
import com.harry.bpm.bean.BpmMatcher;
import com.harry.bpm.bean.BpmOwner;
import com.harry.bpm.bean.BpmProcdef;
import com.harry.bpm.bean.BpmProcdms;
import com.harry.bpm.bean.BpmProctype;
import com.harry.bpm.bean.BpmRole;
import com.harry.bpm.bean.BpmTask;
import com.harry.bpm.bean.BpmTaskRole;
import com.harry.bpm.enums.TaskResult;
import com.harry.bpm.jpa.service.BpmProcdefService;
import com.harry.bpm.jpa.service.BpmProctypeService;
import com.harry.bpm.util.TimeHelpe;
import com.harry.fssc.model.User;
import com.harry.fssc.result.ResponseData;
import com.harry.fssc.result.ResultCode;
import com.harry.fssc.util.Const;

@Component
public class BpmService {

	private static Logger logger = LoggerFactory.getLogger(BpmService.class);
	
	@Autowired
	private UserService userService;
	
	/**
	 * 根据正在执行的任务查询流程实例
	 * @param tasks
	 * @return
	 */
	public List<Map<String, Object>> findProcess(List<Task> tasks) {
		
		List<Map<String, Object>>result=new ArrayList<>();
		try {
			BpmProcdefService procdefService = ActivitiApp.getInstance().initBpmProcdefService();
			BpmProctypeService proctypeService = ActivitiApp.getInstance().initBpmProctypeService();
			Map<String, Object>	info;
			for (int i = 0; i < tasks.size(); i++) {
				TaskEntity task = (TaskEntity)tasks.get(i);
				//流程历史中信息
				info = new HistoryManager().findSingleProcess(task.getProcessInstanceId());
				ProcessInstance process = new ProcessManager().findSingleProcess(task.getProcessInstanceId());
				BpmProcdef procdef = procdefService.findByKey(info.get("bpmKey")+"");
				BpmProctype proctype = proctypeService.findByType(process.getTenantId());
				//流程实例中信息
				if (null!=process) {
					info.put("suspended", process.isSuspended());
				}
				if (null!=procdef) {
					info.put("processName", procdef.getName());
				}
				info.put("formKey", proctype.getForm());
				//任务实例中信息
				info.put("taskId", task.getId());
				info.put("taskName", task.getName());
				info.put("taskType", task.getDescription());
				info.put("taskCreateTime", task.getCreateTime());
				info.put("assignee", task.getAssignee());
				//优先级
				if (task.getPriority()>Const.MID_PRIORITY) {
					info.put("priority", "High");
				}else if (task.getPriority()<Const.MID_PRIORITY) {
					info.put("priority", "Low");
				}else {
					info.put("priority", "Medium");
				}
				//申请人头像
				User user = userService.findUserById(info.get("startUserId").toString());
				info.put("preview", Const.FILE_PREVIEW+user.getPhoto());
				
				result.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Map<String, Object>> findProcess(HashSet<String> ids) {
		List<Map<String, Object>>result=new ArrayList<>();
		BpmProcdefService procdefService = ActivitiApp.getInstance().initBpmProcdefService();
		BpmProctypeService proctypeService = ActivitiApp.getInstance().initBpmProctypeService();
		ProcessManager processManager = new ProcessManager();
		for (String id : ids) {
			Map<String, Object> info=new HistoryManager().findProcessById(id);
			Object instanceId = info.get("instanceId");
			Object tenantId = info.get("tenantId");
			Object bpmKey = info.get("bpmKey");
			Object startUserId=info.get("startUserId");
			if (null==instanceId||null==tenantId||null==bpmKey) {
				continue;
			}
			ProcessInstance process = processManager.findSingleProcess(instanceId.toString());
			BpmProcdef procdef = procdefService.findByKey(bpmKey.toString());
			BpmProctype proctype = proctypeService.findByType(tenantId.toString());
			info.put("isEnded", true);
			if (null!=process) {
				info.put("isEnded", process.isEnded());
				info.put("suspended", process.isSuspended());
			}
			if (null!=procdef) {
				info.put("processName", procdef.getName());
			}
			if (null!=proctype) {
				info.put("formKey", proctype.getForm());
			}
			if (null!=startUserId) {
				String photo = userService.findPhotoById(startUserId.toString());
				if (null!=photo) {
					info.put("preview", Const.FILE_PREVIEW+photo);
				}
			}
			//加载审批节点信息
			List<Map<String, Object>> tasks = this.getHistoryTask(instanceId.toString());
			info.put("tasks", tasks);
			result.add(info);
		}
		return result;
	}
	
	public List<Map<String, Object>> findStarterProcess(List<Map<String,Object>> list) {
		List<Map<String, Object>>result=new ArrayList<>();
		BpmProcdefService procdefService = ActivitiApp.getInstance().initBpmProcdefService();
		BpmProctypeService proctypeService = ActivitiApp.getInstance().initBpmProctypeService();
		ProcessManager processManager = new ProcessManager();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> info=list.get(i);
			Object instanceId = info.get("instanceId");
			Object tenantId = info.get("tenantId");
			Object bpmKey = info.get("bpmKey");
			Object startUserId=info.get("startUserId");
			if (null==instanceId||null==tenantId||null==bpmKey) {
				continue;
			}
			ProcessInstance process = processManager.findSingleProcess(instanceId.toString());
			BpmProcdef procdef = procdefService.findByKey(bpmKey.toString());
			BpmProctype proctype = proctypeService.findByType(tenantId.toString());
			info.put("isEnded", true);
			if (null!=process) {
				info.put("isEnded", process.isEnded());
				info.put("suspended", process.isSuspended());
			}
			if (null!=procdef) {
				info.put("processName", procdef.getName());
			}
			if (null!=proctype) {
				info.put("formKey", proctype.getForm());
			}
			if (null!=startUserId) {
				String photo = userService.findPhotoById(startUserId.toString());
				if (null!=photo) {
					info.put("preview", Const.FILE_PREVIEW+photo);
				}
			}
			//加载审批节点信息
			List<Map<String, Object>> tasks = this.getHistoryTask(instanceId.toString());
			info.put("tasks", tasks);
			result.add(info);
		}
		return result;
	}
	
	public List<Map<String, Object>> getHistoryTask(String instanceId){
		List<Map<String, Object>> result = new ArrayList<>();
		try {
			List<HistoricTaskInstance> tasks = new HistoryManager().findTask(instanceId);
			List<Comment> comments = new TaskManager().findProcessComments(instanceId);
			for (int i = 0; i < tasks.size(); i++) {
				HistoricTaskInstanceEntity t = (HistoricTaskInstanceEntity) tasks.get(i);
				Map<String, Object> persistentState = (Map<String, Object>) t.getPersistentState();
				persistentState.put("startTime", t.getStartTime());
				persistentState.put("claimTime", t.getClaimTime());
				persistentState.put("taskId", t.getId());
				
				if (null!=t.getDurationInMillis()) {
					persistentState.put("durationInMillis", TimeHelpe.getFormatTime(t.getDurationInMillis()));
				}
				//审批结果
				if (null==persistentState.get("deleteReason")) {
					persistentState.put("reasonText", "审批中");
				}else {
					TaskResult[] types = TaskResult.values();
					for (int j = 0; j < types.length; j++) {
						if (types[j].getStatus().equals(persistentState.get("deleteReason"))) {
							persistentState.put("reasonText", types[j].getName());
							break;
						}
					}
				}
				//审批意见
				if (null!=comments) {
					for (int j = 0; j < comments.size(); j++) {
						Comment comment = comments.get(j);
						if (t.getId().equals(comment.getTaskId())) {
							persistentState.put("comment", comment.getFullMessage());
						}
					}
				}
				//审批人头像
				if (null!=t.getAssignee()) {
					String photo = userService.findPhotoById(t.getAssignee());
					if (null!=photo) {
						persistentState.put("preview", Const.FILE_PREVIEW+photo);
					}
				}
				
				result.add(persistentState);
			}
		} catch (Exception e) {
			throw e;
		}
		return result;
	}
	
	public List<BpmDimension> findDimension(){
		return new BpmManager().findDimension();
	}

	public List<BpmDimension> findProcDms(String procType) {
		return new BpmManager().findDimensionByProcType(procType);
	}
	
	public List<Map<String, Object>> findProcMatcher(String formKey) {
		try {
			return new BpmManager().findProcMatcher(formKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<BpmProcdef> findProcdef() {
		try {
			return new BpmManager().findProcdef();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<BpmFilter>findFilter(){
		return new BpmManager().findFilter();
	}

	public Map<String, Object>findFilterByKey(String key){
		return new BpmManager().findByKey(key);
	}
	public List<BpmFilter>findForRelation(String key){
		return new BpmManager().findForRelation(key);
	}
	
	public List<BpmFilterMatcher> findByMatcherFilterKey(String key){
		return new BpmManager().findByMatcherFilterKey(key);
	}
	
	public List<BpmRole>findRoles(){
		return new BpmManager().findRoles();
	}
	
	public List<BpmOwner>findOwnersByRole(String roleid){
		return new BpmManager().findOwnersByRole(roleid);
	}
	
	public List<BpmTask>findTasks(String definitionId){
		return new BpmManager().findTasks(definitionId);
	}
	
	public List<BpmTaskRole>findRoleByTask(String taskId){
		return new BpmManager().findRoleByTask(taskId);
	}
	
	public List<BpmProctype> findCategoryByUser(String userid) {
		return new BpmManager().findCategoryByUser(userid);
	}
	
	public Integer countOwnerByRole(String roleid) {
		return new BpmManager().countOwnerByRole(roleid);
	}
	
	public Integer countTasks(String definitionId){
		return new BpmManager().countTasks(definitionId);
	}

	public void saveDimension(BpmDimension t){
		BpmManager manager = new BpmManager();
		manager.saveDimension(t);
	}
	
	public void saveProcDms(List<BpmProcdms>list) {
		new BpmManager().saveDimensionWithProcType(list);
	}

	public void saveProcMatcher(List<BpmMatcher> t) {
		new BpmManager().saveProcMatcher(t);
	}
	
	public void saveProcdef(BpmProcdef t) {
		new BpmManager().saveProcdef(t);
	}

	public void saveFilter(BpmFilter t) {
		new BpmManager().saveFilter(t);
	}
	
	public void saveFilterMatcher(List<BpmFilterMatcher>ts) {
		new BpmManager().saveFilterMatcher(ts);
	}
	
	public void saveFilterMatcher(BpmFilterMatcher t) {
		new BpmManager().saveFilterMatcher(t);
	}
	
	public void saveRole(BpmRole t) {
		new BpmManager().saveRole(t);
	}
	
	public void saveOwner(BpmOwner t) {
		new BpmManager().saveOwner(t);
	}
	
	public void saveTask(BpmTask t) {
		new BpmManager().saveTask(t);
	}
	
	public void saveTaskRole(BpmTaskRole t) {
		new BpmManager().saveTaskRole(t);
	}
	
	public void saveProctype(BpmProctype t) {
		new BpmManager().saveProctype(t);
	}

	public void deleteDimension(BpmDimension t){
		new BpmManager().deleteDimension(t);
	}
	
	public void deleteProcDms(BpmProcdms procdms) {
		new BpmManager().deleteDimensionByProcType(procdms);
	}
	
	public void deleteProcMatcher(BpmMatcher t) {
		new BpmManager().deleteProcMatcher(t);
	}
	
	public void deleteProcdef(BpmProcdef t) {
		new BpmManager().deleteProcdef(t);
	}
	
	public void deleteFilter(BpmFilter t) {
		new BpmManager().deleteFilter(t);
	}
	
	public void deleteFilterMatcher(BpmFilterMatcher t) {
		new BpmManager().deleteFilterMatcher(t);
	}
	
	public void deleteFilterMatcher(String key) {
		new BpmManager().deleteFilterMatcher(key);
	}
	
	public void deleteRole(BpmRole t) {
		new BpmManager().deleteRole(t);
	}
	
	public void deleteOwner(BpmOwner t) {
		new BpmManager().deleteOwner(t);
	}
	
	public void deleteOwnerByRole(String roleid) {
		new BpmManager().deleteByRole(roleid);
	}
	
	public void deleteTask(BpmTask t) {
		new BpmManager().deleteTask(t);
	}
	
	public void deleteTaskRole(BpmTaskRole t) {
		new BpmManager().deleteTaskRole(t);
	}
}
