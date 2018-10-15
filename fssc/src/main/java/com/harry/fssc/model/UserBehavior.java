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
@Table(schema = "DBUSER", name = "T_USER_BEHAVIOR")
public class UserBehavior implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER_BEHAVIOR")
	@SequenceGenerator(sequenceName = "T_USER_BEHAVIOR_SEQ", allocationSize = 1, name = "SEQ_USER_BEHAVIOR")
	private Long id;
	@Column(name="USER_ID")
	private String userId;
	@Column(name="SESSION_ID")
	private String sessionId;
	@Column(name="PATH_")
	private String path;
	@Column(name="NAME_")
	private String name;
	@Column(name="ADDR_")
	private String addr;
	@Column(name="HOST_")
	private String host;
	@Column(name="MSG_")
	private String msg;
	@Column(name="DATE_",insertable = false)
	private Timestamp date;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public UserBehavior(String userId, String sessionId, String path, String name, String addr, String host,
			String msg) {
		super();
		this.userId = userId;
		this.sessionId = sessionId;
		this.path = path;
		this.name = name;
		this.addr = addr;
		this.host = host;
		this.msg = msg;
	}
	public UserBehavior(String userId, String sessionId, String path, String name, String addr, String host) {
		super();
		this.userId = userId;
		this.sessionId = sessionId;
		this.path = path;
		this.name = name;
		this.addr = addr;
		this.host = host;
	}
	public UserBehavior() {
		super();
	}
	
	
}
