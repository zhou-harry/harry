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
@Table(schema = "DBUSER", name = "T_BPM_ROLE")
public class BpmRole implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ID", insertable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BPM_TASK_ROLE")
	@SequenceGenerator(sequenceName = "T_BPM_ROLE_SEQ", allocationSize = 1, name = "SEQ_BPM_TASK_ROLE")
	private Long id;

	@Id
	@Column(name = "ROLEID")
	private String roleId;
	
	@Column(name = "NAME_")
	private String name;
	
	@Column(name = "TYPE_")
	private Integer type;
	@Transient
	private Integer countOwners;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCountOwners() {
		return countOwners;
	}

	public void setCountOwners(Integer countOwners) {
		this.countOwners = countOwners;
	}

	@Override
	public String toString() {
		return "BpmRole [id=" + id + ", roleId=" + roleId + ", name=" + name + ", type=" + type + "]";
	}
	
}
