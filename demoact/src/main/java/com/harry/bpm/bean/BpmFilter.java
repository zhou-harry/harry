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
@Table(schema = "DBUSER", name = "T_BPM_FILTER")
public class BpmFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ID", insertable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BPM_FILTER")
	@SequenceGenerator(sequenceName = "T_BPM_FILTER_SEQ", allocationSize = 1, name = "SEQ_BPM_FILTER")
	private Long id;
	
	@Id
	@Column(name = "FILTER_ID")
	private String filterId;
	
	@Column(name = "NAME_")
	private String name;
	
	@Column(name = "TYPE_")
	private Integer type;
	
	@Column(name = "MASTER")
	private String master;
	
	@Column(name = "SLAVE")
	private String slave;
	
	@Column(name = "RELATION")
	private Integer relation;
	@Transient
	private List<BpmFilterMatcher>items;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFilterId() {
		return filterId;
	}

	public void setFilterId(String filterId) {
		this.filterId = filterId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getSlave() {
		return slave;
	}

	public void setSlave(String slave) {
		this.slave = slave;
	}

	public Integer getRelation() {
		return relation;
	}

	public void setRelation(Integer relation) {
		this.relation = relation;
	}

	public List<BpmFilterMatcher> getItems() {
		return items;
	}

	public void setItems(List<BpmFilterMatcher> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "BpmFilter [id=" + id + ", filterId=" + filterId + ", name=" + name + ", type=" + type + ", master="
				+ master + ", slave=" + slave + ", relation=" + relation + "]";
	}
	

}
