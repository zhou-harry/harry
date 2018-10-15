package com.harry.bpm.bean.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BpmMatcherPK implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "PROC_TYPE")
    private String procType;
	@Column(name = "DMS_KEY")
	private String dmsKey;
	@Column(name = "PROC_KEY")
	private String procKey;
	
	public BpmMatcherPK() {
		super();
	}
	public BpmMatcherPK(String procType, String dmsKey, String procKey) {
		super();
		this.procType = procType;
		this.dmsKey = dmsKey;
		this.procKey = procKey;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dmsKey == null) ? 0 : dmsKey.hashCode());
		result = prime * result + ((procType == null) ? 0 : procType.hashCode());
		result = prime * result + ((procKey == null) ? 0 : procKey.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BpmMatcherPK))
			return false;
		BpmMatcherPK other = (BpmMatcherPK) obj;
		if (dmsKey == null) {
			if (other.dmsKey != null)
				return false;
		} else if (!dmsKey.equals(other.dmsKey))
			return false;
		if (procType == null) {
			if (other.procType != null)
				return false;
		} else if (!procType.equals(other.procType))
			return false;
		if (procKey == null) {
			if (other.procKey != null)
				return false;
		} else if (!procKey.equals(other.procKey))
			return false;
		return true;
	}
	
}
