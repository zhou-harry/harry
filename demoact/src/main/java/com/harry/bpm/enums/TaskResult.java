package com.harry.bpm.enums;
/**
 * 任务执行状态
 * @author harry
 *
 */
public enum TaskResult {

	COMPLETE("COMPLETE","完成"), 
	REJECT("REJECT","驳回"), 
	PENDING("PENDING","加签"), 
	COPY_READ("COPY_READ","已阅"),
	JOB_DELEGETE("JOB_DELEGETE","自动委托");

	TaskResult(String status,String name) {
		this.status = status;
		this.name=name;
	}

	private String status;
	private String name;

	
	public String getName() {
		return name;
	}


	public String getStatus() {
		return status;
	}

}
