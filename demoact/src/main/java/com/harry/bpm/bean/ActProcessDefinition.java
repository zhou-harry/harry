package com.harry.bpm.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActProcessDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	private final static SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	
	private String id;
	private String deploymentId;
	private String deploymentName;
	private String name;
	private String key;
	private String description;
	private Integer version;
	private String diagramResourceName;
	private String suspended;
	private String deployTime;
	private String tenantId;

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setDeployTime(String deployTime) {
		this.deployTime = deployTime;
	}

	public String getDeployTime() {
		return deployTime;
	}

	public void setDeployTime(Date deployTime) {
		this.deployTime = formatter.format(deployTime);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getDiagramResourceName() {
		return diagramResourceName;
	}

	public void setDiagramResourceName(String diagramResourceName) {
		this.diagramResourceName = diagramResourceName;
	}

	public String getSuspended() {
		return suspended;
	}

	public void setSuspended(String suspended) {
		this.suspended = suspended;
	}

	public String getDeploymentName() {
		return deploymentName;
	}

	public void setDeploymentName(String deploymentName) {
		this.deploymentName = deploymentName;
	}

	@Override
	public String toString() {
		return "ActProcessDefinition [id=" + id + ", deploymentId=" + deploymentId + ", deploymentName="
				+ deploymentName + ", name=" + name + ", key=" + key + ", description=" + description + ", version="
				+ version + ", diagramResourceName=" + diagramResourceName + ", suspended=" + suspended
				+ ", deployTime=" + deployTime + ", tenantId=" + tenantId + "]";
	}

}
