package com.harry.bpm.bean.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BpmTaskRolePK implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "PROC_DEF_ID")
	private String definitionId;
	
	@Column(name = "TASK_DEF_ID")
	private String taskId;
	
	@Column(name = "ROLEID")
	private String roleId;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getDefinitionId() {
		return definitionId;
	}

	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((definitionId == null) ? 0 : definitionId.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BpmTaskRolePK))
			return false;
		BpmTaskRolePK other = (BpmTaskRolePK) obj;
		if (definitionId == null) {
			if (other.definitionId != null)
				return false;
		} else if (!definitionId.equals(other.definitionId))
			return false;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		return true;
	}
	
	
}
