package com.example.demoact;

import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Deployment;
import org.junit.Test;

import com.harry.ActivitiApp;
import com.harry.bpm.BpmManager;
import com.harry.bpm.RepositoryManager;
import com.harry.bpm.bean.BpmOwner;
import com.harry.bpm.bean.BpmTask;


public class AppTest {
	
	@Test
	public void tempTest() {
		Deployment d = new RepositoryManager().deployment("测试", "T001");
		System.out.println(d.getId()+"/"+d.getName());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
