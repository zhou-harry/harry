package com.harry.bpm.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(schema="DBUSER", name = "T_BPM_DIMENSION")
public class BpmDimension implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ID", insertable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BPM_DIMENSION")
	@SequenceGenerator(sequenceName = "T_BPM_DIMENSION_SEQ", allocationSize = 1, name = "SEQ_BPM_DIMENSION")
	private Long id;

	@Id
	@Column(name = "KEY_")
	private String key;

	@Column(name = "NAME_")
	private String name;

	@Column(name = "TYPE_")
	private String type;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "BpmDimension [id=" + id + ", key=" + key + ", name=" + name + ", type=" + type + "]";
	}

}
