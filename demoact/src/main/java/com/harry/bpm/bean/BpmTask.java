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

import com.harry.bpm.bean.pk.BpmTaskPK;

@Entity
@Table(schema = "DBUSER", name = "T_BPM_TASK_DEF")
@IdClass(BpmTaskPK.class)
public class BpmTask implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ID", insertable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BPM_TASK_DEF")
	@SequenceGenerator(sequenceName = "T_BPM_TASK_DEF_SEQ", allocationSize = 1, name = "SEQ_BPM_TASK_DEF")
	private Long id;
	@Id
	@Column(name = "PROC_DEF_ID")
	private String definitionId;
	@Id
	@Column(name = "TASK_DEF_ID")
	private String taskId;
	
	@Column(name = "FILTER_ID")
	private String filterId;
	@Transient
	private String filterName;
	
	@Column(name = "TASK_NAME")
	private String name;
	@Column(name = "TASK_TYPE")
	private Integer type;
	@Column(name = "STEP")
	private Integer step;
	@Column(name = "ISSELECT")
	private Boolean isSelect;
	@Column(name = "RATIO")
	private Integer ratio;
	@Column(name = "STATUS")
	private Integer status;
	@Column(name = "DURATION")
	private Integer duration;
	@Column(name = "STANDBY")
	private String standby;
	@Column(name = "PENDING")
	private Boolean pending;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDefinitionId() {
		return definitionId;
	}
	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
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
	public Integer getStep() {
		return step;
	}
	public void setStep(Integer step) {
		this.step = step;
	}
	public Boolean getIsSelect() {
		return null==isSelect?false:isSelect;
	}
	public void setIsSelect(Boolean isSelect) {
		this.isSelect = isSelect;
	}
	public Integer getRatio() {
		return ratio;
	}
	public void setRatio(Integer ratio) {
		this.ratio = ratio;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public String getStandby() {
		return standby;
	}
	public void setStandby(String standby) {
		this.standby = standby;
	}
	public Boolean getPending() {
		return null==pending?false:pending;
	}
	public void setPending(Boolean pending) {
		this.pending = pending;
	}
	
	public String getFilterName() {
		return filterName;
	}
	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
	@Override
	public String toString() {
		return "BpmTask [id=" + id + ", definitionId=" + definitionId + ", taskId=" + taskId + ", filterId=" + filterId
				+ ", filterName=" + filterName + ", name=" + name + ", type=" + type + ", step=" + step + ", isSelect="
				+ isSelect + ", ratio=" + ratio + ", status=" + status + ", duration=" + duration + ", standby="
				+ standby + ", pending=" + pending + "]";
	}
	
	
	
}
