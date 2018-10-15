package com.harry.bpm.enums;
/**
 * 任务状态
 * @author harry
 *
 */
public enum TaskState {

	ACTIVE(1,"生效"),
	INACTIVE(2, "无效"), 
	REMOVE(3, "删除"), 
	;
	
	private Integer key;
	private String name;

	private TaskState(Integer key, String name) {
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
