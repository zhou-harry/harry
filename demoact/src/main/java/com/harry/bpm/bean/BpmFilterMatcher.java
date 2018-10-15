package com.harry.bpm.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.harry.bpm.bean.pk.BpmFilterMatcherPK;

@Entity
@Table(schema = "DBUSER", name = "T_BPM_FILTER_MATCHER")
@IdClass(BpmFilterMatcherPK.class)
public class BpmFilterMatcher implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ID", insertable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BPM_FILTER_MATCHER")
	@SequenceGenerator(sequenceName = "T_BPM_FILTER_MATCHER_SEQ", allocationSize = 1, name = "SEQ_BPM_FILTER_MATCHER")
	private Long id;

	@Id
	@Column(name = "FILTER_ID")
	private String filterId;

	@Id
	@Column(name = "DMS_KEY")
	private String dmsKey;

	@Column(name = "MATCHER")
	private String matcher;

	@Column(name = "RANGE_S")
	private BigDecimal rangeStart;

	@Column(name = "RANGE_E")
	private BigDecimal rangeEnd;

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

	public String getDmsKey() {
		return dmsKey;
	}

	public void setDmsKey(String dmsKey) {
		this.dmsKey = dmsKey;
	}

	public String getMatcher() {
		return matcher;
	}

	public void setMatcher(String matcher) {
		this.matcher = matcher;
	}

	public BigDecimal getRangeStart() {
		return rangeStart;
	}

	public void setRangeStart(BigDecimal rangeStart) {
		this.rangeStart = rangeStart;
	}

	public BigDecimal getRangeEnd() {
		return rangeEnd;
	}

	public void setRangeEnd(BigDecimal rangeEnd) {
		this.rangeEnd = rangeEnd;
	}

	@Override
	public String toString() {
		return "BpmFilterMatcher [id=" + id + ", filterId=" + filterId + ", dmsKey=" + dmsKey + ", matcher=" + matcher
				+ ", rangeStart=" + rangeStart + ", rangeEnd=" + rangeEnd + "]";
	}

}
