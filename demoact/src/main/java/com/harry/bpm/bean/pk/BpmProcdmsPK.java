package com.harry.bpm.bean.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BpmProcdmsPK implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "PROC_TYPE")
    private String procType;
     
    @Column(name = "DMS_KEY")
    private String dmsKey;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dmsKey == null) ? 0 : dmsKey.hashCode());
		result = prime * result + ((procType == null) ? 0 : procType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BpmProcdmsPK))
			return false;
		BpmProcdmsPK other = (BpmProcdmsPK) obj;
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
		return true;
	}

	@Override
	public String toString() {
		return "BpmProcdmsKey [procType=" + procType + ", dmsKey=" + dmsKey + "]";
	}

	public BpmProcdmsPK(String procType, String dmsKey) {
		super();
		this.procType = procType;
		this.dmsKey = dmsKey;
	}

	public BpmProcdmsPK() {
		super();
	}
    
}
