package com.harry.bpm.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(schema = "DBUSER", name = "T_BPM_PROC_DEF")
public class BpmProcdef implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ID", insertable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BPM_PROC_DEF")
	@SequenceGenerator(sequenceName = "T_BPM_PROC_DEF_SEQ", allocationSize = 1, name = "SEQ_BPM_PROC_DEF")
	private Long id;

	@Id
	@Column(name = "KEY_")
	private String key;

	@Column(name = "NAME_")
	private String name;
	
	@Column(name = "DEPLOYMENT_KEY")
	private String deploymentKey;
	
	@Transient
	private Integer countTasks;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public Integer getCountTasks() {
		return countTasks;
	}

	public void setCountTasks(Integer countTasks) {
		this.countTasks = countTasks;
	}

	public String getDeploymentKey() {
		return deploymentKey;
	}

	public void setDeploymentKey(String deploymentKey) {
		this.deploymentKey = deploymentKey;
	}

	@Override
	public String toString() {
		return "BpmProcdef [id=" + id + ", key=" + key + ", name=" + name + ", countTasks=" + countTasks + "]";
	}

	

}
