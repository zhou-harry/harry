package com.harry.bpm.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.Task;

import com.harry.bpm.util.Constant;

/**
 * 单签任务JOB启动监听
 * 
 * @author harry
 *
 */
public class SingleJobService implements JavaDelegate {

	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("当前审批超过5秒未审批,系统进行短信,微信,邮件等联系审批人及时审批!");
		
	}

}
