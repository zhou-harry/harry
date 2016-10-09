package com.harry.entity;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Menu implements RowMapper<Menu>, Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;// id
	private String menuid;// 菜单id
	private String name;
	private String url;
	private String parentId;
	private Integer priority;
	private String folder;
	private String img;
	private String style;
	private String typeId;

	public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
		Menu bean = new Menu();
		bean.setId(rs.getLong("ID"));
		bean.setMenuid(rs.getString("MENUID"));
		bean.setName(rs.getString("NAME"));
		bean.setUrl(rs.getString("URL"));
		bean.setParentId(rs.getString("PARENTID"));
		bean.setPriority(rs.getInt("PRIORITY"));
		bean.setFolder(rs.getString("ISFOLDER"));
		bean.setImg(rs.getString("IMG_SRC"));
		bean.setStyle(rs.getString("CLASS_SRC"));
		bean.setTypeId(rs.getString("TYPEID"));
		return bean;
	}

	@Override
	public String toString() {
		return "Menu [id=" + id + ", menuid=" + menuid + ", name=" + name
				+ ", url=" + url + ", parentId=" + parentId + ", priority="
				+ priority + ", folder=" + folder + ", img=" + img + ", style="
				+ style + ", typeId=" + typeId + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

}
