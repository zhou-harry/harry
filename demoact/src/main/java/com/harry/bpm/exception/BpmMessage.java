package com.harry.bpm.exception;

public enum BpmMessage {
	
	S0000("S0000","操作成功！"),
	
	E0000("E0000","操作失败！"),
	E0001("E0001","无效的流程实例编号！"),
	E0002("E0002","无效的流程模板!"),
	E0003("E0003","当前版本下存在有效的审批信息!"),
	E0004("E0004","无效的流程实例/任务实例!"),
	E0005("E0005","无效的执行实例!"),
	E0006("E0006","无效的任务实例!"),
	E0007("E0007","无效的附件实例!"),
	E0008("E0008","流程参数不匹配,流程创建失败."),
	E0009("E0009","根据流程类型:{0},未查询到流程模板."),
	
	W0000("W0000","查询结果为空！"),
	W0001("W0001","找不到要删除的数据！"),
	;

	private String code;
	private String name;

	private BpmMessage(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
}
