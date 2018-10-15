package com.harry.fssc.model;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
@Entity
@Table(schema = "DBUSER", name = "T_ATTACHMENT")
public class Attachment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_")
	private String id;
	@Column(name = "NAME_")
	private String name;
	@Column(name = "TYPE_")
	private String type;
	@Lob 
	@Basic(fetch=FetchType.LAZY) 
	@Column(name = "RES_")
	private byte[] resource;
	@Column(name = "STATE_")
	private Integer state;
	@Column(name="DATE_",insertable = false)
    private Timestamp date;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getResource() {
		return resource;
	}

	public void setResource(byte[] resource) {
		this.resource = resource;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

}
