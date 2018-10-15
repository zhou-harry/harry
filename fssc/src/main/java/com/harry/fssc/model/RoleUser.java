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
@Table(schema = "DBUSER", name = "T_ROLE_USER")
public class RoleUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ROLE_USER")
	@SequenceGenerator(sequenceName = "T_ROLE_USER_SEQ", allocationSize = 1, name = "SEQ_ROLE_USER")
	private Long id;
	@Column(name = "ROLEID", unique = true)
	private String roleId;
	@Column(name="USERID",unique = true)
	private String userId;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public RoleUser() {
		super();
	}
	public RoleUser(String roleId, String userId) {
		super();
		this.roleId = roleId;
		this.userId = userId;
	}
	
}
