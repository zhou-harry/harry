package com.harry.bpm.registry;
/**
 * 注册事件
 * @author harry
 *
 */

import java.io.Serializable;

import com.harry.bpm.client.ExecutionAction;
import com.harry.bpm.client.ExecutionProxy;
import com.harry.bpm.client.TaskAction;
import com.harry.bpm.client.TaskProxy;

public class ActivitiRegistry implements Serializable {

	private static final long serialVersionUID = 1L;

	// 流程启动事件
	private ExecutionProxy processStart = null;
	// 流程结束事件
	private ExecutionProxy processEnd = null;
	// 任务排他网关
	private ExecutionProxy exclusiveTask = null;
	// 任务抄送网关
	private ExecutionProxy executionCopy = null;
	// 无下一步任务事件
	private ExecutionProxy executionNoTask = null;
	// 业务pending事件
	private ExecutionProxy exclusiveReceive = null;
	// 任务驳回事件
	private ExecutionProxy exclusiveRejected = null;
	// 单签定时器监听
	private ExecutionProxy exclusiveSingleJob = null;

	// 单签任务启动事件
	private TaskProxy singleTaskStart = null;
	// 单签任务结束事件
	private TaskProxy singleTaskEnd = null;
	// 会签任务启动事件
	private TaskProxy multiTaskStart = null;
	// 会签任务结束事件
	private TaskProxy multiTaskEnd = null;
	// 抄送任务启动事件
	private TaskProxy copyTaskStart = null;
	// 抄送任务结束事件
	private TaskProxy copyTaskEnd = null;

	public ExecutionAction getProcessStart() {
		return processStart;
	}

	public void setProcessStart(ExecutionAction processStart) {
		this.processStart = new ExecutionProxy(processStart);
	}

	public ExecutionAction getProcessEnd() {
		return processEnd;
	}

	public void setProcessEnd(ExecutionAction processEnd) {
		this.processEnd = new ExecutionProxy(processEnd);
	}

	public TaskAction getSingleTaskStart() {
		return singleTaskStart;
	}

	public void setSingleTaskStart(TaskAction singleTaskStart) {
		this.singleTaskStart = new TaskProxy(singleTaskStart);
	}

	public TaskAction getSingleTaskEnd() {
		return singleTaskEnd;
	}

	public void setSingleTaskEnd(TaskAction singleTaskEnd) {
		this.singleTaskEnd = new TaskProxy(singleTaskEnd);
	}

	public TaskAction getMultiTaskStart() {
		return multiTaskStart;
	}

	public void setMultiTaskStart(TaskAction multiTaskStart) {
		this.multiTaskStart = new TaskProxy(multiTaskStart);
	}

	public TaskAction getMultiTaskEnd() {
		return multiTaskEnd;
	}

	public void setMultiTaskEnd(TaskAction multiTaskEnd) {
		this.multiTaskEnd = new TaskProxy(multiTaskEnd);
	}

	public TaskAction getCopyTaskStart() {
		return copyTaskStart;
	}

	public void setCopyTaskStart(TaskAction copyTaskStart) {
		this.copyTaskStart = new TaskProxy(copyTaskStart);
	}

	public TaskAction getCopyTaskEnd() {
		return copyTaskEnd;
	}

	public void setCopyTaskEnd(TaskAction copyTaskEnd) {
		this.copyTaskEnd = new TaskProxy(copyTaskEnd);
	}

	public ExecutionAction getExclusiveTask() {
		return exclusiveTask;
	}

	public void setExclusiveTask(ExecutionAction exclusiveTask) {
		this.exclusiveTask = new ExecutionProxy(exclusiveTask);
	}

	public ExecutionAction getExecutionCopy() {
		return executionCopy;
	}

	public void setExecutionCopy(ExecutionAction executionCopy) {
		this.executionCopy = new ExecutionProxy(executionCopy);
	}

	public ExecutionAction getExecutionNoTask() {
		return executionNoTask;
	}

	public void setExecutionNoTask(ExecutionAction executionNoTask) {
		this.executionNoTask = new ExecutionProxy(executionNoTask);
	}

	public ExecutionAction getExclusiveReceive() {
		return exclusiveReceive;
	}

	public void setExclusiveReceive(ExecutionAction exclusiveReceive) {
		this.exclusiveReceive = new ExecutionProxy(exclusiveReceive);
	}

	public ExecutionAction getExclusiveRejected() {
		return exclusiveRejected;
	}

	public void setExclusiveRejected(ExecutionAction exclusiveRejected) {
		this.exclusiveRejected = new ExecutionProxy(exclusiveRejected);
	}

	public ExecutionAction getExclusiveSingleJob() {
		return exclusiveSingleJob;
	}

	public void setExclusiveSingleJob(ExecutionAction exclusiveSingleJob) {
		this.exclusiveSingleJob = new ExecutionProxy(exclusiveSingleJob);
	}

}
