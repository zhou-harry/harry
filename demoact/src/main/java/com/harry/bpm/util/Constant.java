package com.harry.bpm.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程常量参数
 * @author harry
 *
 */
public class Constant {

	public static final String ACT_PROC_DEFID="ACT_PROC_DEFID";
	
	public static final String ACT_TASK_CURRENT_OBJECT="ACT_TASK_CURRENT";
	public static final String ACT_TASK_RESULT="ACT_TASK_RESULT";
	public static final String ACT_TASK_MULTI="ACT_TASK_MULTI";
	public static final String ACT_TASK_NEXT="ACT_TASK_NEXT";
	public static final String ACT_TASK_TIMER="ACT_TASK_TIMER";
	public static final String ACT_TASK_PENDING="ACT_TASK_PENDING";
	
	public static final String ACT_ACTION="ACT_ACTION";
	public static final String ACT_AGREED_COUNT="ACT_AGREED_COUNT";
	public static final String ACT_CANDIDATE_LIST="ACT_CANDIDATE_LIST";
	public static final String ACT_COPY_TYPE="ACT_COPY_TYPE";
	public static final String ACT_COPY_ROLES="ACT_COPY_LIST";
	public static final String ACT_APPR_ADMIN="ACT_ADMIN";
	
	public static final String BPM_PROC_TYPE_ROOT="T";
	
	public static List<String> ACT_INNER=new ArrayList<String>();
	
	static {
		ACT_INNER.add("nrOfActiveInstances");
		ACT_INNER.add("loopCounter");
		ACT_INNER.add("copy");
		ACT_INNER.add("nrOfInstances");
		ACT_INNER.add("nrOfCompletedInstances");
		ACT_INNER.add("candidate");
	}
	
}
