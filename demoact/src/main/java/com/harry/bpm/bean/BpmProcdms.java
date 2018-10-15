package com.harry.bpm.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.harry.bpm.bean.pk.BpmProcdmsPK;

@Entity
@Table(schema="DBUSER", name = "T_BPM_PROC_DMS")
public class BpmProcdms implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BPM_PROC_DMS")
	@SequenceGenerator(sequenceName = "T_BPM_PROC_DMS_SEQ", allocationSize = 1, name = "SEQ_BPM_PROC_DMS")
	private Long id;
	//复合主键
	@EmbeddedId
    private BpmProcdmsPK idkey;
	@Transient
	private String typeName;
	@Transient
	private String dmsDesc;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BpmProcdmsPK getIdkey() {
		return idkey;
	}
	public void setIdkey(BpmProcdmsPK idkey) {
		this.idkey = idkey;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getDmsDesc() {
		return dmsDesc;
	}
	public void setDmsDesc(String dmsDesc) {
		this.dmsDesc = dmsDesc;
	}
	
	
}
