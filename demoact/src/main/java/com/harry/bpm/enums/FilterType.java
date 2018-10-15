package com.harry.bpm.enums;
/**
 * @author harry
 *过滤器类型
 */
public enum FilterType {

	FORCE(1, "默认"), 
	DEFINE(2, "自定义"), 
	REF(3, "引用");

	private Integer key;
	private String name;

	private FilterType(Integer key, String name) {
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
