package com.harry.bpm.enums;
/**
 * @author harry
 *引用关系（1：交集、2：并集、3：求反）
 */
public enum MergeType {

	INTERSECT(1, "交集"), 
	UNION(2, "并集"), 
	EXCEPT(3, "排斥");

	private Integer key;
	private String name;

	private MergeType(Integer key, String name) {
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
