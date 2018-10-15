package com.harry.bpm.enums;
/**
 * 任务类型
 * @author harry
 *
 */
public enum TaskType {

	APPLY(1,"申请"),
	SINGLE(2, "单签"), 
	MULTI(3, "会签"), 
	ADDITIONAL(4, "加签"),
	COPY(5,"抄送"),
	SCHEDULE(6,"系统调度"),
	;
	
	private Integer key;
	private String name;

	private TaskType(Integer key, String name) {
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
