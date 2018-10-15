package com.harry.bpm.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.harry.bpm.bean.pk.BpmMatcherPK;

@Entity
@Table(schema="DBUSER", name = "T_BPM_PROC_MATCHER")
@IdClass(BpmMatcherPK.class)
public class BpmMatcher implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "ID",insertable=false,updatable=false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BPM_MATCHER")
	@SequenceGenerator(sequenceName = "T_BPM_PROC_MATCHER_SEQ", allocationSize = 1, name = "SEQ_BPM_MATCHER")
	private Long id;

	@Id
	@Column(name = "PROC_TYPE")
    private String procType;
	@Id
	@Column(name = "DMS_KEY")
	private String dmsKey;
	@Id
	@Column(name = "PROC_KEY")
	private String procKey;
	
	@Column(name = "MATCHER")
	private String matcher;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProcType() {
		return procType;
	}

	public void setProcType(String procType) {
		this.procType = procType;
	}

	public String getDmsKey() {
		return dmsKey;
	}

	public void setDmsKey(String dmsKey) {
		this.dmsKey = dmsKey;
	}

	public String getProcKey() {
		return procKey;
	}

	public void setProcKey(String procKey) {
		this.procKey = procKey;
	}

	public String getMatcher() {
		return matcher;
	}

	public void setMatcher(String matcher) {
		this.matcher = matcher;
	}
	
	
}
