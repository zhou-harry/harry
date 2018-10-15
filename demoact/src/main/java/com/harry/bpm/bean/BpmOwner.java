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
import javax.persistence.Transient;

import com.harry.bpm.bean.pk.BpmOwnerPK;

@Entity
@Table(schema = "DBUSER", name = "T_BPM_ROLE_OWNER")
@IdClass(BpmOwnerPK.class)
public class BpmOwner implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ID", insertable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BPM_ROLE_OWNER")
	@SequenceGenerator(sequenceName = "T_BPM_ROLE_OWNER_SEQ", allocationSize = 1, name = "SEQ_BPM_ROLE_OWNER")
	private Long id;

	@Id
	@Column(name = "ROLEID")
	private String roleId;
	@Id
	@Column(name = "OWNER_ID")
	private String ownerId;
	
	@Column(name = "FILTER_ID")
	private String filterId;
	@Transient
	private String ownerName;
	@Transient
	private String filterName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getFilterId() {
		return filterId;
	}
	public void setFilterId(String filterId) {
		this.filterId = filterId;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	public String getFilterName() {
		return filterName;
	}
	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
	@Override
	public String toString() {
		return "BpmOwner [id=" + id + ", roleId=" + roleId + ", ownerId=" + ownerId + ", filterId=" + filterId
				+ ", ownerName=" + ownerName + ", filterName=" + filterName + "]";
	}
	
	
	
}
