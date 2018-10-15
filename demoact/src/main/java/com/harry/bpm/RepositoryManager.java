package com.harry.bpm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;

import com.harry.ActivitiApp;
import com.harry.bpm.bean.ActProcessDefinition;
import com.harry.bpm.exception.BpmException;
import com.harry.bpm.exception.BpmMessage;
import com.harry.bpm.util.Constant;

public class RepositoryManager {

	private ProcessEngine processEngine;

	private RepositoryService repositoryService;

	public RepositoryManager() {
		processEngine = ActivitiApp.getInstance().initProcessEngine();
		repositoryService = processEngine.getRepositoryService();
	}
	
	/**
	 * 流程模板注册
	 * 
	 * @param name
	 *            注册名称
	 * @return
	 */
	public Deployment deployment(String name) {
		Deployment deployment = repositoryService.createDeployment().name(name)
				.addClasspathResource("diagrams/native.bpmn").deploy();

		return deployment;
	}
	/**
	 * 流程模板注册
	 * @param name 名称
	 * @param tenantId 类型
	 * @return
	 */
	public Deployment deployment(String name,String tenantId) {
		Deployment deployment = repositoryService.createDeployment()
				.name(name)
				.tenantId(tenantId)
				.addClasspathResource("diagrams/native.bpmn").deploy();
		return deployment;
	}
	
	public Deployment deployment(String name,String category,String tenantId) {
		Deployment deployment = repositoryService.createDeployment()
				.name(name)
				.category(category)
				.tenantId(tenantId)
				.addClasspathResource("diagrams/native.bpmn").deploy();
		return deployment;
	}
	
	public List<Deployment> findDeployments(String tenantId) {
		List<Deployment> list = repositoryService.createDeploymentQuery()
				.deploymentTenantId(tenantId)
				.list();
		return list;
	} 

	/**
	 * 根据部署id查找部署信息
	 * 
	 * @param deploymentId
	 * @return
	 */
	public Deployment findDeployment(String deploymentId) {
		Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
		return deployment;
	}

	/**
	 * 查询流程资源图
	 * 
	 * @param deploymentId
	 * @param imageName
	 * @return
	 */
	public InputStream findImageInputStream(String deploymentId) {
		String resourceName = this.findByDeploymentId(deploymentId).getDiagramResourceName();
		return repositoryService.getResourceAsStream(deploymentId, resourceName);
	}

	/**
	 * 删除流程定义
	 * @param deploymentId
	 * @param cascade 是否强制删除
	 */
	public void deleteDeployment(String deploymentId,boolean cascade) {
		try {
			repositoryService.deleteDeployment(deploymentId,cascade);
		} catch (ActivitiObjectNotFoundException e) {
			throw new BpmException(BpmMessage.E0002, e);
		} catch (RuntimeException e2) {
			throw new BpmException(BpmMessage.E0003, e2);
		}
	}

	/**
	 * 查询系统中所有部署的流程定义
	 * 
	 * @return
	 */
	public List<ActProcessDefinition> findAll() {

		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionKey()
				.asc().orderByProcessDefinitionVersion().desc().list();

		List<ActProcessDefinition> result = new ArrayList<ActProcessDefinition>();
		if (list != null && list.size() > 0) {
			for (ProcessDefinition pd : list) {
				result.add(formartDefinition(pd));
			}
		}
		return result;
	}

	/**
	 * 查询系统中所有部署的流程定义(只看最新版本)
	 */
	public List<ActProcessDefinition> findAllLastVersion() {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().latestVersion()
				.orderByProcessDefinitionName().asc().list();

		List<ActProcessDefinition> result = new ArrayList<ActProcessDefinition>();
		if (list != null && list.size() > 0) {
			for (ProcessDefinition pd : list) {
				result.add(formartDefinition(pd));
			}
		}
		return result;
	}
	
	public List<ActProcessDefinition> findLastVersionByTenant(String tenantId) {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
				.processDefinitionTenantId(tenantId)
				.latestVersion()
				.orderByProcessDefinitionName().asc().list();

		List<ActProcessDefinition> result = new ArrayList<ActProcessDefinition>();
		if (list != null && list.size() > 0) {
			for (ProcessDefinition pd : list) {
				result.add(formartDefinition(pd));
			}
		}
		return result;
	}
	
	public ActProcessDefinition findLastVersionBy(String tenantId,String processDefinitionKey) {
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
				.processDefinitionTenantId(tenantId)
				.processDefinitionKey(processDefinitionKey)
				.latestVersion()
				.singleResult();

		return formartDefinition(pd);
	}

	public ActProcessDefinition findByDeploymentId(String deploymentId) {
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId)
				.singleResult();

		return formartDefinition(pd);
	}

	/**
	 * 根据流程模板查询
	 * 
	 * @param processDefinitionKey
	 * @return
	 */
	public List<ActProcessDefinition> findByProcessDefinitionKey(String processDefinitionKey) {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(processDefinitionKey).orderByProcessDefinitionVersion().desc().list();
		
		List<ActProcessDefinition> result = new ArrayList<ActProcessDefinition>();
		if (list != null && list.size() > 0) {
			for (ProcessDefinition pd : list) {
				result.add(formartDefinition(pd));
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param tenantId
	 * @return
	 */
	public List<ActProcessDefinition> findByTenantId(String tenantId) {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
				.processDefinitionTenantId(tenantId)
				.orderByProcessDefinitionVersion().desc().list();
		
		List<ActProcessDefinition> result = new ArrayList<ActProcessDefinition>();
		if (list != null && list.size() > 0) {
			for (ProcessDefinition pd : list) {
				result.add(formartDefinition(pd));
			}
		}
		return result;
	}
	
	public long countByTenantId(String tenantId) {
		return repositoryService.createProcessDefinitionQuery()
				.processDefinitionTenantId(tenantId)
				.latestVersion()
				.count();
	}

	private ActProcessDefinition formartDefinition(ProcessDefinition pd) {
		ActProcessDefinition bean = new ActProcessDefinition();
		if (null==pd) {
			return bean;
		}
		
		Deployment deployment = this.findDeployment(pd.getDeploymentId());

		bean.setId(pd.getId());
		bean.setName(pd.getName());
		bean.setDescription(pd.getDescription());
		bean.setKey(pd.getKey());
		bean.setVersion(pd.getVersion());
		bean.setDiagramResourceName(pd.getDiagramResourceName());
		bean.setDeploymentId(pd.getDeploymentId());
		bean.setDeploymentName(deployment.getName());
		bean.setSuspended(pd.isSuspended() ? "Y" : "N");
		bean.setTenantId(pd.getTenantId());
		bean.setDeployTime(deployment.getDeploymentTime());
		
		return bean;
	}
}
