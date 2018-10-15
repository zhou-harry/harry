package com.harry.bpm.bean.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BpmFilterMatcherPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "FILTER_ID")
	private String filterId;

	@Column(name = "DMS_KEY")
	private String dmsKey;

	public String getFilterId() {
		return filterId;
	}

	public void setFilterId(String filterId) {
		this.filterId = filterId;
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
		result = prime * result + ((filterId == null) ? 0 : filterId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BpmFilterMatcherPK))
			return false;
		BpmFilterMatcherPK other = (BpmFilterMatcherPK) obj;
		if (dmsKey == null) {
			if (other.dmsKey != null)
				return false;
		} else if (!dmsKey.equals(other.dmsKey))
			return false;
		if (filterId == null) {
			if (other.filterId != null)
				return false;
		} else if (!filterId.equals(other.filterId))
			return false;
		return true;
	}

}
