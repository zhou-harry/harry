package com.harry.bpm.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(schema="DBUSER",name = "ACT_CUS_TASKINST")
public class CusTaskinst implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACT_CUS_TASKINST_SEQ")
	@SequenceGenerator(sequenceName = "ACT_CUS_TASKINST_SEQ", allocationSize = 1, name = "ACT_CUS_TASKINST_SEQ")
	private Long id;
	@Column(name = "PROC_INST_ID")
	private String procInstId;
	@Column(name = "EXECUTION_ID")
	private String executionId;
	@Column(name = "TASK_INST_ID")
	private String taskInstId;
	@Column(name = "DELETE_REASON")
	private String deleteReason;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProcInstId() {
		return procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getTaskInstId() {
		return taskInstId;
	}

	public void setTaskInstId(String taskInstId) {
		this.taskInstId = taskInstId;
	}

	public String getDeleteReason() {
		return deleteReason;
	}

	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}

	public CusTaskinst(String procInstId, String executionId, String taskInstId, String deleteReason) {
		this.procInstId = procInstId;
		this.executionId = executionId;
		this.taskInstId = taskInstId;
		this.deleteReason = deleteReason;
	}

	public CusTaskinst() {
		super();
	}

}
