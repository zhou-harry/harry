package com.harry.fssc.model.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class StartInstance implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String procType;
	private String businessKey;
	private String description;
	private List<Variables> variables;
	
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Variables> getVariables() {
		return variables;
	}
	public void setVariables(List<Variables> variables) {
		this.variables = variables;
	}
	public String getProcType() {
		return procType;
	}
	public void setProcType(String procType) {
		this.procType = procType;
	}

}
