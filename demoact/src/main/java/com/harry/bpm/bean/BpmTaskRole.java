package com.harry.bpm.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.harry.bpm.bean.pk.BpmTaskRolePK;

@Entity
@Table(schema = "DBUSER", name = "T_BPM_TASK_ROLE")
@IdClass(BpmTaskRolePK.class)
public class BpmTaskRole {

	@Column(name = "ID", insertable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BPM_TASK_ROLE")
	@SequenceGenerator(sequenceName = "T_BPM_TASK_ROLE_SEQ", allocationSize = 1, name = "SEQ_BPM_TASK_ROLE")
	private Long id;
	
	@Id
	@Column(name = "PROC_DEF_ID")
	private String definitionId;
	@Id
	@Column(name = "TASK_DEF_ID")
	private String taskId;
	@Id
	@Column(name = "ROLEID")
	private String roleId;
	
	@Column(name = "FILTER_ID")
	private String filterId;
	
	@Transient
	private String roleName;
	
	@Transient
	private String filterName;

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

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getFilterId() {
		return filterId;
	}

	public void setFilterId(String filterId) {
		this.filterId = filterId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "BpmTaskRole [definitionId=" + definitionId + ", taskId=" + taskId + ", roleId=" + roleId + ", filterId="
				+ filterId + ", roleName=" + roleName + ", filterName=" + filterName + "]";
	}
	
	
}
