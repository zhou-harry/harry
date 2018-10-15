package com.harry.bpm.bean;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(schema="DBUSER", name = "T_BPM_PROC_TYPE")
public class BpmProctype implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name="ID", insertable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BPM_PROC_TYPE")
	@SequenceGenerator(sequenceName = "T_BPM_PROC_TYPE_SEQ", allocationSize = 1, name = "SEQ_BPM_PROC_TYPE")
	private Long id;
	
	@Id
	@Column(name="TYPE_")
	private String type;
	
	@Column(name="NAME_")
	private String name;
	
	@Column(name="PARENT_")
	private String parent;
	
	@Column(name="FORM_")
	private String form;
	
	@Transient
	private String formName;
	
	@Transient
	private List<BpmProctype> items; 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public List<BpmProctype> getItems() {
		return items;
	}

	public void setItems(List<BpmProctype> items) {
		this.items = items;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	@Override
	public String toString() {
		return "BpmProctype [id=" + id + ", type=" + type + ", name=" + name + ", parent=" + parent +", items=" + (null==items?-1:items.size()) + "]";
	}
	
}
