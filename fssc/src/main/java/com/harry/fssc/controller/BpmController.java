package com.harry.fssc.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harry.ActivitiApp;
import com.harry.bpm.BpmManager;
import com.harry.bpm.HistoryManager;
import com.harry.bpm.ProcessManager;
import com.harry.bpm.RepositoryManager;
import com.harry.bpm.TaskManager;
import com.harry.bpm.bean.ActProcessDefinition;
import com.harry.bpm.bean.BpmProcdef;
import com.harry.bpm.bean.BpmProctype;
import com.harry.bpm.enums.TaskResult;
import com.harry.bpm.jpa.service.BpmProcdefService;
import com.harry.bpm.jpa.service.BpmProctypeService;
import com.harry.bpm.registry.ActivitiRegistry;
import com.harry.bpm.util.Constant;
import com.harry.bpm.util.TimeHelpe;
import com.harry.fssc.aop.logger.BehaviorManage;
import com.harry.fssc.enums.AspectType;
import com.harry.fssc.listener.bpm.ExclusiveReceive;
import com.harry.fssc.listener.bpm.ExclusiveRejected;
import com.harry.fssc.listener.bpm.ExclusiveSingleJob;
import com.harry.fssc.listener.bpm.ExclusiveTask;
import com.harry.fssc.listener.bpm.ExecutionCopy;
import com.harry.fssc.listener.bpm.ExecutionNoTask;
import com.harry.fssc.listener.bpm.ProcessEnded;
import com.harry.fssc.listener.bpm.ProcessStarted;
import com.harry.fssc.listener.bpm.TaskCopyEnd;
import com.harry.fssc.listener.bpm.TaskCopyStart;
import com.harry.fssc.listener.bpm.TaskMultiEnd;
import com.harry.fssc.listener.bpm.TaskMultiStart;
import com.harry.fssc.listener.bpm.TaskSingleEnd;
import com.harry.fssc.listener.bpm.TaskSingleStart;
import com.harry.fssc.model.User;
import com.harry.fssc.model.dto.StartInstance;
import com.harry.fssc.model.dto.Variables;
import com.harry.fssc.result.ResponseData;
import com.harry.fssc.result.ResultCode;
import com.harry.fssc.service.BpmService;
import com.harry.fssc.service.UserService;
import com.harry.fssc.session.SessionUtil;
import com.harry.fssc.util.Const;
import com.harry.fssc.util.StreamUtil;

@RestController
@RequestMapping("bpm")
public class BpmController {

	private static Logger logger = LoggerFactory.getLogger(BpmController.class);
	
	@Autowired
	private BpmService service;
	@Autowired
	private UserService userService;
	@Autowired
	private SessionUtil sessionUtil;

	@RequestMapping(value = "deploy", method = RequestMethod.POST)
	@BehaviorManage(type=AspectType.BPM_DEPLOY)
	public ResponseData deployment(HttpServletRequest request, HttpServletResponse response, String name,String type) {

		Deployment deployment = new RepositoryManager().deployment(name,type);

		return new ResponseData(ResultCode.SUCCESS, deployment);

	}
	
	@RequestMapping(value = "deleteDeploy", method = RequestMethod.POST)
	@BehaviorManage(type=AspectType.BPM_DEPLOY_DELETE)
	public ResponseData deleteDeploy(HttpServletRequest request, HttpServletResponse response, String deployId) {

		new RepositoryManager().deleteDeployment(deployId, true);
		
		return new ResponseData(ResultCode.SUCCESS, null);
	}

	@RequestMapping(value = "deployDiagram", method = RequestMethod.GET)
	public void deployDiagram(HttpServletRequest request, HttpServletResponse response, String deployId)
			throws IOException {
		InputStream inputStream = new RepositoryManager().findImageInputStream(deployId);

		response.setHeader("content-disposition", "online;filename=" + deployId + ".png");// 设置响应头 attachment
		response.setContentType("image/jpg");
		OutputStream toClient = response.getOutputStream(); // 得到向客户端输出二进制数据的对象
		InputStream bis = new BufferedInputStream(inputStream);
		toClient.write(new StreamUtil().getBytes(bis)); // 输出数据
		bis.close();
		toClient.close();
	}
	
	@RequestMapping(value = "allVersion", method = RequestMethod.POST)
	public ResponseData allVersion(HttpServletRequest request, HttpServletResponse response) {

		RepositoryManager manager = new RepositoryManager();

		List<ActProcessDefinition> result = manager.findAll();

		return new ResponseData(ResultCode.SUCCESS, result);
	}

	@RequestMapping(value = "allLastVersion", method = RequestMethod.POST)
	public ResponseData allLastVersion(HttpServletRequest request, HttpServletResponse response) {

		RepositoryManager manager = new RepositoryManager();

		List<ActProcessDefinition> result = manager.findAllLastVersion();

		return new ResponseData(ResultCode.SUCCESS, result);
	}
	
	@RequestMapping(value = "lastVersionByType", method = RequestMethod.POST)
	public ResponseData lastVersionByType(HttpServletRequest request, HttpServletResponse response,String type) {

		RepositoryManager manager = new RepositoryManager();

		List<ActProcessDefinition> result = manager.findLastVersionByTenant(type);

		return new ResponseData(ResultCode.SUCCESS, result);
	}

	@RequestMapping(value = "allProcessDef", method = RequestMethod.POST)
	public ResponseData allProcessDefinition(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "type") String type) {

		List<ActProcessDefinition> result = new RepositoryManager().findByTenantId(type);

		return new ResponseData(ResultCode.SUCCESS, result);
	}

	@RequestMapping(value = "sizeInstance", method = RequestMethod.POST)
	public ResponseData sizeInstance(HttpServletRequest request, HttpServletResponse response, String pdid) {

		long sizeInstance = new ProcessManager().countProcess(pdid,null, null);

		long sizeHistory = new HistoryManager().sizeProcess(pdid, null, null);

		Long[] size = { sizeInstance, sizeHistory };

		return new ResponseData(ResultCode.SUCCESS, size);
	}

	@RequestMapping(value = "startProcess", method = RequestMethod.POST)
	@BehaviorManage(type=AspectType.BPM_STARTPROCESS)
	public ResponseData startProcess(HttpServletRequest request, HttpServletResponse response,
			@RequestBody StartInstance instance) {

		if (null == instance) {
			return new ResponseData(ResultCode.FAILED, "参数为空.");
		}
		Map<String, Object> variables = new HashMap<String, Object>();
		List<Variables> list = instance.getVariables();
		if (null != list) {
			for (int i = 0; i < list.size(); i++) {
				Variables v = list.get(i);
				variables.put(v.get_key(), v.get_value());
			}
		}
		ActivitiRegistry registry = new ActivitiRegistry();
		registry.setProcessStart(new ProcessStarted());
		registry.setExclusiveReceive(new ExclusiveReceive());
		registry.setExclusiveRejected(new ExclusiveRejected());
		registry.setExclusiveSingleJob(new ExclusiveSingleJob());
		registry.setExclusiveTask(new ExclusiveTask());
		registry.setExecutionCopy(new ExecutionCopy());
		registry.setExecutionNoTask(new ExecutionNoTask());
		registry.setSingleTaskStart(new TaskSingleStart());
		registry.setSingleTaskEnd(new TaskSingleEnd());
		registry.setMultiTaskStart(new TaskMultiStart());
		registry.setMultiTaskEnd(new TaskMultiEnd());
		registry.setCopyTaskStart(new TaskCopyStart());
		registry.setCopyTaskEnd(new TaskCopyEnd());
		registry.setProcessEnd(new ProcessEnded());

		User userInfo = sessionUtil.getUserInfo(request, response);
		
		String piid = new ProcessManager().startProcessInstance(instance.getProcType(), instance.getBusinessKey(),
				instance.getDescription(),userInfo.getUserId(), variables, registry);

		return new ResponseData(ResultCode.SUCCESS, piid);
	}

	@RequestMapping(value = "procInstances", method = RequestMethod.POST)
	public ResponseData allProcInstance(HttpServletRequest request, HttpServletResponse response, String pdid,
			String bsid, String piid,String name) {
		List<Map<String, Object>>result=new ArrayList<>();
		ProcessManager processManager = new ProcessManager();
		try {
			pdid=("undefined".equals(pdid)||"null".equals(pdid))?null:pdid;
			bsid=("undefined".equals(bsid)||"null".equals(bsid))?null:bsid;
			piid=("undefined".equals(piid)||"null".equals(piid))?null:piid;
			name=("undefined".equals(name)||"null".equals(name))?null:name;
			result = new HistoryManager().findProcess(pdid, piid, bsid,name);
			BpmProcdefService procdefService = ActivitiApp.getInstance().initBpmProcdefService();

			for (int i = 0; i < result.size(); i++) {
				Map<String, Object> info = result.get(i);
				Object instanceId = info.get("instanceId");
				if (null==instanceId) {
					continue;
				}
				ProcessInstance process = processManager.findSingleProcess(instanceId.toString());
				BpmProcdef procdef = procdefService.findByKey(info.get("bpmKey")+"");
				
				info.put("isEnded", true);
				if (null!=process) {
					info.put("isEnded", process.isEnded());
					info.put("suspended", process.isSuspended());
				}
				if (null!=procdef) {
					info.put("processName", procdef.getName());
				}
			}
			return new ResponseData(ResultCode.SUCCESS, result);
		} catch (Exception e) {
			return new ResponseData(ResultCode.FAILED, e.getMessage());
		}
	}

	@RequestMapping(value = "findCategory", method = RequestMethod.POST)
	public ResponseData findCategory(HttpServletRequest request, HttpServletResponse response) {
		User userInfo = sessionUtil.getUserInfo(request, response);
		try {
			List<Map<String,Object>> list=new ArrayList<>();
			List<BpmProctype> types = service.findCategoryByUser(userInfo.getUserId());
			Map<String,Object> obj;
			TaskManager taskManager = new TaskManager();
			HistoryManager historyManager = new HistoryManager();
			for (int i = 0; i < types.size(); i++) {
				BpmProctype t = types.get(i);
				obj=new HashMap<>();
				obj.put("type", t.getType());
				obj.put("name", t.getName());
				
				long sizeProcessByOwner = historyManager.sizeProcessByOwner(t.getType(),userInfo.getUserId());
				long sizeProcessByStarter = historyManager.sizeProcessByStarter(t.getType(),userInfo.getUserId());
				
				obj.put("countHis", sizeProcessByOwner+sizeProcessByStarter);
				
				obj.put("count", taskManager.countTask(userInfo.getUserId(), t.getType()));
				
				list.add(obj);
			}
			return new ResponseData(ResultCode.SUCCESS, list);
		} catch (Exception e) {
			return new ResponseData(ResultCode.FAILED, e.getMessage());
		}
	}
	
	@RequestMapping(value = "findAssigneeProcess", method = RequestMethod.POST)
	public ResponseData findAssigneeProcess(HttpServletRequest request, HttpServletResponse response,String type) {
		User userInfo = sessionUtil.getUserInfo(request, response);
		try {
			List<Task> tasks = new TaskManager().findAssigneeTask(userInfo.getUserId(),type);
			
			List<Map<String, Object>>result=service.findProcess(tasks);
			
			return new ResponseData(ResultCode.SUCCESS, result);
		} catch (Exception e) {
			return new ResponseData(ResultCode.FAILED, e.getMessage());
		}
	}
	
	@RequestMapping(value = "findCandidateProcess", method = RequestMethod.POST)
	public ResponseData findCandidateProcess(HttpServletRequest request, HttpServletResponse response,String type) {
		User userInfo = sessionUtil.getUserInfo(request, response);
		try {
			List<Task> tasks = new TaskManager().findCandidateTask(userInfo.getUserId(),type);
			
			List<Map<String, Object>>result=service.findProcess(tasks);
			
			return new ResponseData(ResultCode.SUCCESS, result);
		} catch (Exception e) {
			return new ResponseData(ResultCode.FAILED, e.getMessage());
		}
	}
	
	@RequestMapping(value = "findTaskProcess", method = RequestMethod.POST)
	public ResponseData findTaskProcess(HttpServletRequest request, HttpServletResponse response,String type) {
		User userInfo = sessionUtil.getUserInfo(request, response);
		try {
			List<Task> tasks = new TaskManager().findTask(userInfo.getUserId(),type);
			
			List<Map<String, Object>>result=service.findProcess(tasks);
			
			return new ResponseData(ResultCode.SUCCESS, result);
		} catch (Exception e) {
			return new ResponseData(ResultCode.FAILED, e.getMessage());
		}
	}
	
	@RequestMapping(value = "findHistoryProcess", method = RequestMethod.POST)
	public ResponseData findHistoryProcess(HttpServletRequest request, HttpServletResponse response,String type) {
		User userInfo = sessionUtil.getUserInfo(request, response);
		try {
			HashSet<String> ids = new HistoryManager().findProcessIdByOwner(type, userInfo.getUserId());
			
			List<Map<String, Object>>result=service.findProcess(ids);
			
			return new ResponseData(ResultCode.SUCCESS, result);
		} catch (Exception e) {
			return new ResponseData(ResultCode.FAILED, e.getMessage());
		}
	}
	
	@RequestMapping(value = "findStarterProcess", method = RequestMethod.POST)
	public ResponseData findStarterProcess(HttpServletRequest request, HttpServletResponse response,String type) {
		User userInfo = sessionUtil.getUserInfo(request, response);
		try {
			List<Map<String,Object>> list = new HistoryManager().findProcessByStarter(type, userInfo.getUserId());
			
			List<Map<String, Object>>result=service.findStarterProcess(list);
			
			return new ResponseData(ResultCode.SUCCESS, result);
		} catch (Exception e) {
			return new ResponseData(ResultCode.FAILED, e.getMessage());
		}
	}

	@RequestMapping(value = "procVariables", method = RequestMethod.POST)
	public ResponseData getProcVariables(HttpServletRequest request, HttpServletResponse response, String piid) {

		List<Variables> result = new ArrayList<Variables>();
		try {
			Map<String, Object> vs = new HistoryManager().getVariables(piid);
			Set<String> keys = vs.keySet();
			for (String k : keys) {
				result.add(new Variables(k, vs.get(k)));
			}
			return new ResponseData(ResultCode.SUCCESS, result);
		} catch (Exception e) {
			return new ResponseData(ResultCode.FAILED, e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "historyTask", method = RequestMethod.POST)
	public ResponseData getHistoryTask(HttpServletRequest request, HttpServletResponse response, String piid) {
		try {
			List<Map<String, Object>> result = service.getHistoryTask(piid);
			
			return new ResponseData(ResultCode.SUCCESS, result);
		} catch (Exception e) {
			return new ResponseData(ResultCode.FAILED, e.getMessage());
		}

	}

	@RequestMapping(value = "findCoording", method = RequestMethod.POST)
	public ResponseData findCoording(HttpServletRequest request, HttpServletResponse response, String piid) {

		try {
			List<Map<String, Object>> list = new ProcessManager().findCoording(piid);
			return new ResponseData(ResultCode.SUCCESS, list);
		} catch (Exception e) {
			return new ResponseData(ResultCode.FAILED, e.getMessage());
		}

	}

	@RequestMapping(value = "findExecution", method = RequestMethod.POST)
	public ResponseData findTaskByExecution(HttpServletRequest request, HttpServletResponse response,
			String executionId) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("key", executionId);
			Task task = new TaskManager().findSingleTask(null, executionId);
			if (null != task) {
				result.put("type", task.getTaskDefinitionKey());
				result.put("id", task.getId());
				result.put("name", task.getName());
				result.put("assignee", task.getAssignee());
				result.put("owner", task.getOwner());
				result.put("createTime", task.getCreateTime());
				result.put("taskDefKey", task.getTaskDefinitionKey());
			} else {
				ProcessManager processManager = new ProcessManager();
				List<ExecutionEntity> list = processManager.findExecution(null, executionId);
				for (ExecutionEntity entity : list) {
					List<ActivityImpl> activity;
					if (entity instanceof ProcessInstance) {
						activity = processManager.findActivity(entity);
					} else {
						activity = processManager.findActivity(entity.getProcessInstanceId());
					}
					for (ActivityImpl activityImpl : activity) {
						result.put("type", activityImpl.getProperty("type"));
						result.put("name", activityImpl.getProperty("documentation"));
					}
				}
			}
			return new ResponseData(ResultCode.SUCCESS, result);
		} catch (Exception e) {
			return new ResponseData(ResultCode.FAILED, e.getMessage());
		}
	}

	@RequestMapping(value = "completeSignal", method = RequestMethod.POST)
	public ResponseData completeSignal(HttpServletRequest request, HttpServletResponse response, String executionId,@RequestBody List<Variables> variables) {

		//变量上下文
		Map<String, Object> vs=null;
		if (null!=variables&&variables.size()>0) {
			vs=new HashMap<>();
			for (int i = 0; i < variables.size(); i++) {
				Variables v = variables.get(i);
				vs.put(v.get_key(), v.get_value());
			}
		}
		new TaskManager().completeSignal(executionId,vs);
		
		return new ResponseData(ResultCode.SUCCESS, null);
	}

	@RequestMapping(value = "completeTask", method = RequestMethod.POST)
	public ResponseData completeTask(HttpServletRequest request, HttpServletResponse response, String piid,
			String taskId, String action, String comment, @RequestBody List<Variables> variables) {
		
		TaskResult tResult = null;
		if (TaskResult.COMPLETE.getStatus().equals(action)) {
			tResult = TaskResult.COMPLETE;
		} else if (TaskResult.REJECT.getStatus().equals(action)) {
			tResult = TaskResult.REJECT;
		} else if (TaskResult.COPY_READ.getStatus().equals(action)) {
			tResult = TaskResult.COPY_READ;
		} else {
			return new ResponseData(ResultCode.FAILED, "操作状态无效!");
		}
		User userInfo = sessionUtil.getUserInfo(request, response);
		//变量上下文
		Map<String, Object> vs=null;
		if (null!=variables&&variables.size()>0) {
			vs=new HashMap<>();
			for (int i = 0; i < variables.size(); i++) {
				Variables v = variables.get(i);
				vs.put(v.get_key(), v.get_value());
			}
		}
		new TaskManager().completeTask(piid, taskId, tResult, comment, userInfo.getUserId(),vs);

		return new ResponseData(ResultCode.SUCCESS, null);
	}
	
	@RequestMapping(value = "commitTask", method = RequestMethod.POST)
	public ResponseData commitTask(HttpServletRequest request, HttpServletResponse response, String piid,
			String taskId, String action, String comment) {
		
		TaskResult tResult = null;
		if (TaskResult.COMPLETE.getStatus().equals(action)) {
			tResult = TaskResult.COMPLETE;
		} else if (TaskResult.REJECT.getStatus().equals(action)) {
			tResult = TaskResult.REJECT;
		} else if (TaskResult.COPY_READ.getStatus().equals(action)) {
			tResult = TaskResult.COPY_READ;
		} else {
			return new ResponseData(ResultCode.FAILED, "操作状态无效!");
		}
		User userInfo = sessionUtil.getUserInfo(request, response);
		new TaskManager().completeTask(piid, taskId, tResult, comment, userInfo.getUserId(),null);

		return new ResponseData(ResultCode.SUCCESS, null);
	}

	@RequestMapping(value = "assigneeTask", method = RequestMethod.POST)
	public ResponseData assigneeTask(HttpServletRequest request, HttpServletResponse response, String taskId,
			String userId) {
		new TaskManager().assigneeTask(taskId, userId);
		
		return new ResponseData(ResultCode.SUCCESS, null);
	}

	@RequestMapping(value = "ownerTask", method = RequestMethod.POST)
	public ResponseData ownerTask(HttpServletRequest request, HttpServletResponse response, String taskId,
			String userId, Boolean pending) {
		
		new TaskManager().ownerTask(taskId, userId, pending);
		
		return new ResponseData(ResultCode.SUCCESS, null);
	}
	
	@RequestMapping(value = "removeVariable", method = RequestMethod.POST)
	public ResponseData removeVariable(HttpServletRequest request, HttpServletResponse response, String taskId,
			String variableName) {
		
		new TaskManager().removeVariable(taskId, variableName);
		
		return new ResponseData(ResultCode.SUCCESS, null);
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "uploadAttachment", method = RequestMethod.POST)
	public ResponseData uploadAttachment(HttpServletRequest request, HttpServletResponse response) {
		String name = request.getHeader("name");
		String notes = request.getHeader("notes");
		String piid = request.getHeader("piid");
		String taskid = request.getHeader("taskid");
		try {
			name=URLDecoder.decode(name,request.getCharacterEncoding());
			piid=URLDecoder.decode(piid,request.getCharacterEncoding());
			if (null!=notes) {
				notes=URLDecoder.decode(notes,request.getCharacterEncoding());
			}
			if (null!=taskid) {
				taskid=URLDecoder.decode(taskid,request.getCharacterEncoding());
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return new ResponseData(ResultCode.FAILED, e1.getMessage());
		}  
		try {
			byte[] b=new byte[request.getContentLength()];
			ServletInputStream inputStream = request.getInputStream();
			DataInputStream in = new DataInputStream(request.getInputStream());
	        in.readFully(b);
	        in.close();
	        
	        User userInfo = sessionUtil.getUserInfo(request, response);
	        
	        new TaskManager().addAttachment(piid, taskid,userInfo.getUserId(), request.getContentType(), name, notes, new ByteArrayInputStream(b));
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseData(ResultCode.FAILED, e.getMessage());
		}
		return new ResponseData(ResultCode.SUCCESS, "OK");
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "uploadAttachmentUrl", method = RequestMethod.POST)
	public ResponseData uploadAttachmentUrl(HttpServletRequest request, HttpServletResponse response) {

		String name = request.getHeader("name");
		String notes = request.getHeader("notes");
		try {
			name=URLDecoder.decode(name,request.getCharacterEncoding());
			notes=URLDecoder.decode(notes,request.getCharacterEncoding());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return new ResponseData(ResultCode.FAILED, e1.getMessage());
		}  
		try {
			byte[] b=new byte[request.getContentLength()];
			ServletInputStream inputStream = request.getInputStream();
			DataInputStream in = new DataInputStream(request.getInputStream());
	        in.readFully(b);
	        in.close();
	        BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream("D:\\upload\\"+name));
	        out.write(b);
	        out.flush();
	        out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseData(ResultCode.FAILED, e.getMessage());
		}
		return new ResponseData(ResultCode.SUCCESS, "OK");
	}
	
	@RequestMapping(value = "attachments", method = RequestMethod.POST)
	public ResponseData attachments(HttpServletRequest request, HttpServletResponse response, String piid, String taskid) {
		List<Attachment> attachments;
		List<Map<String, Object>>result=new ArrayList<>();
		try {
			Map<String, Object>obj;
			if (null==taskid) {
				attachments = new TaskManager().getProcessInstanceAttachments(piid);
			}else {
				attachments = new TaskManager().getTaskAttachments(taskid);
			}
			if (null!=attachments) {
				for (int i = 0; i < attachments.size(); i++) {
					obj=new HashMap<>();
					Attachment attachment = attachments.get(i);
					String url=attachment.getUrl();
					if (null==url) {
						String contextPath = request.getContextPath();
						String scheme = request.getScheme();
						String serverName = request.getServerName();
						int serverPort = request.getServerPort();
						url=scheme+"://"+serverName+":"+serverPort+contextPath+"/bpm/attachmentUrl?attachmentId="+attachment.getId();
					}
					obj.put("documentId", attachment.getId());
					obj.put("fileName", attachment.getName());
					obj.put("mimeType", attachment.getType());
					obj.put("url", url);
					obj.put("description", attachment.getDescription());
					obj.put("taskId", attachment.getTaskId());
					obj.put("time", attachment.getTime());
					obj.put("user", attachment.getUserId());
					result.add(obj);
				}
			}
			return new ResponseData(ResultCode.SUCCESS, result);
		} catch (Exception e) {
			return new ResponseData(ResultCode.FAILED, e.getMessage());
		}
	}
	
	@RequestMapping(value = "attachmentDel", method = RequestMethod.POST)
	public ResponseData attachmentDel(HttpServletRequest request, HttpServletResponse response, String attachmentId) {
		new TaskManager().removeAttachment(attachmentId);
		return new ResponseData(ResultCode.SUCCESS, null);
	}
	
	@RequestMapping(value = "attachmentUrl", method = RequestMethod.GET)
	public void attachmentUrl(HttpServletRequest request, HttpServletResponse response, String attachmentId)
			throws IOException {
		TaskManager taskManager = new TaskManager();
		Attachment attachment = taskManager.getAttachment(attachmentId);
		InputStream inputStream = taskManager.getAttachmentContent(attachmentId);
		// 设置响应头 attachment
		String contentDisposition="attachment";
		if (null!=attachment.getType()) {
			contentDisposition="inline";
			response.setContentType(attachment.getType());
		}
		response.setHeader("content-disposition", contentDisposition+";filename=" + URLEncoder.encode(attachment.getName(),"UTF-8"));
		// 得到向客户端输出二进制数据的对象
		OutputStream toClient = response.getOutputStream(); 
		InputStream bis = new BufferedInputStream(inputStream);
		// 输出数据
		toClient.write(new StreamUtil().getBytes(bis)); 
		bis.close();
		toClient.close();
	}
	
	@RequestMapping(value = "findTaskComments", method = RequestMethod.POST)
	public ResponseData findTaskComments(HttpServletRequest request, HttpServletResponse response, String taskId,String type) {
		List<Map<String, Object>>result=new ArrayList<>();
		try {
			List<Comment> comments = new TaskManager().findProcessComments(taskId);
			if (null!=comments) {
				Map<String, Object>obj;
				for (int i = 0; i < comments.size(); i++) {
					obj=new HashMap<>();
					Comment comment = comments.get(i);
					obj.put("user", comment.getUserId());
					obj.put("message", comment.getFullMessage());
					obj.put("time", comment.getTime());
					obj.put("taskid", comment.getTaskId());
					TaskResult[] types = TaskResult.values();
					for (int j = 0; j < types.length; j++) {
						if (types[j].getStatus().equals(comment.getType())) {
							obj.put("type", types[j].getName());
							break;
						}
					}
					result.add(obj);
				}
			}
			return new ResponseData(ResultCode.SUCCESS, result);
		} catch (Exception e) {
			return new ResponseData(ResultCode.FAILED, e.getMessage());
		}

	}
	
	@RequestMapping(value = "procTypeTree", method = RequestMethod.POST)
	public ResponseData findProcTypeTree(HttpServletRequest request, HttpServletResponse response) {
		BpmManager manager = new BpmManager();
		
		List<BpmProctype> list = manager.findProctypeByParent(null,true);
		
		List<Map<String, Object>>result=new ArrayList<>();
		
		this.items(list,result);
		
		return new ResponseData(ResultCode.SUCCESS, result);
	}
	
	
	private long items(List<BpmProctype> list,List<Map<String, Object>>result){
		long c =0;
		if (list!=null&&list.size()>0) {
			RepositoryManager repositoryManager = new RepositoryManager();
			Map<String, Object>m;
			for (int i = 0; i < list.size(); i++) {
				BpmProctype p = list.get(i);
				m=new HashMap<>();
				m.put("type", p.getType());
				m.put("name", p.getName());
				m.put("form", p.getForm());
				m.put("formName", p.getFormName());
				m.put("parent", p.getParent());
				
				List<Map<String, Object>>items=new ArrayList<>();
				long sc=items(p.getItems(),items);
				m.put("items", items);
				
				long count= repositoryManager.countByTenantId(p.getType());
				m.put("deploymentCount", count);
				m.put("deploymentTotal", sc);
				result.add(m);
				c+=count;
			}
		}
		return c;
	}
}
