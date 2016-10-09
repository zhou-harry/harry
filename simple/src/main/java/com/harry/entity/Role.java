package com.harry.entity;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Role implements RowMapper<Role>, Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String roleid;
	private String rolename;
	private String parentid;
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", roleid=" + roleid + ", rolename="
				+ rolename + ", parentid=" + parentid + ", status=" + status
				+ "]";
	}

	public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
		Role bean = new Role();
		bean.setId(rs.getLong("ID"));
		bean.setRoleid(rs.getString("ROLEID"));
		bean.setRolename(rs.getString("ROLENAME"));
		bean.setParentid(rs.getString("PARENT_RID"));
		bean.setStatus(rs.getInt("STATUS"));
		return bean;
	}

}
