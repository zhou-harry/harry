package com.harry.entity;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Tree implements RowMapper<Tree>, Serializable {
	private static final long serialVersionUID = 1L;

	private Integer level;
	private Integer isleaf;
	private String id;
	private String title;
	private String parentid;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(Integer isleaf) {
		this.isleaf = isleaf;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	@Override
	public String toString() {
		return "Tree [level=" + level + ", isleaf=" + isleaf + ", id=" + id
				+ ", title=" + title + ", parentid=" + parentid + "]";
	}

	public Tree mapRow(ResultSet rs, int rowNum) throws SQLException {
		Tree bean = new Tree();
		bean.setLevel(rs.getInt(1));
		bean.setIsleaf(rs.getInt(2));
		bean.setId(rs.getString(3));
		bean.setTitle(rs.getString(4));
		bean.setParentid(rs.getString(5));
		return bean;
	}

}
