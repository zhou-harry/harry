package com.harry.bpm.enums;
/**
 * 角色类型
 * @author harry
 *
 */
public enum RoleType {

	TASK(1,"审批角色"),
	NOTIFY(2, "邮件通知角色"), 
	COPY(3, "抄送角色"), 
	;
	
	private Integer key;
	private String name;

	private RoleType(Integer key, String name) {
		this.key = key;
		this.name = name;
	}

	public Integer getKey() {
		return key;
	}

	public String getName() {
		return name;
	}
}
