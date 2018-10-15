package com.harry.fssc.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(schema = "DBUSER", name = "T_ROLE")
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ROLE")
	@SequenceGenerator(sequenceName = "T_ROLE_SEQ", allocationSize = 1, name = "SEQ_ROLE")
	private Long id;
	@Column(name = "ROLEID", unique = true)
	private String roleId;
	@Column(name = "ROLENAME")
	private String name;
	@Column(name = "PARENT_RID")
	private String parent;
	@Column(name = "STATUS")
	private Boolean status;
	@Column(name = "SYS")
	private Boolean isSys;
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CREATE_TIME", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp created;
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
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Boolean getIsSys() {
		return isSys;
	}
	public void setIsSys(Boolean isSys) {
		this.isSys = isSys;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	@Override
	public String toString() {
		return "Role [roleId=" + roleId + ", name=" + name + "]";
	}
	
}
