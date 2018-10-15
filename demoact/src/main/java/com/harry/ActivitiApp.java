package com.harry;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.harry.bpm.jpa.service.BpmDimensionService;
import com.harry.bpm.jpa.service.BpmFilterMatcherService;
import com.harry.bpm.jpa.service.BpmFilterService;
import com.harry.bpm.jpa.service.BpmMatcherService;
import com.harry.bpm.jpa.service.BpmOwnerService;
import com.harry.bpm.jpa.service.BpmProcdefService;
import com.harry.bpm.jpa.service.BpmProcdmsService;
import com.harry.bpm.jpa.service.BpmProctypeService;
import com.harry.bpm.jpa.service.BpmRoleService;
import com.harry.bpm.jpa.service.BpmTaskRoleService;
import com.harry.bpm.jpa.service.BpmTaskService;
import com.harry.bpm.jpa.service.CusTaskinstService;

public class ActivitiApp {

	private static ActivitiApp instance;

	private ApplicationContext context;

	private ProcessEngine processEngine;

	public static ActivitiApp getInstance() {
		if (instance == null) {
			synchronized (ActivitiApp.class) {
				if (instance == null)
					instance = new ActivitiApp();
			}
		}
		return instance;
	}

	private ActivitiApp() {
		context = new ClassPathXmlApplicationContext("spring-jpa-cfg.xml");
		processEngine = ProcessEngines.getDefaultProcessEngine();
	}

	public ProcessEngine initProcessEngine() {
		return processEngine;
	}

	public CusTaskinstService initCusTaskinstService() {
		return context.getBean("cusTaskinstService", CusTaskinstService.class);
	}

	public BpmProctypeService initBpmProctypeService() {
		return context.getBean("bpmProctypeService", BpmProctypeService.class);
	}

	public BpmDimensionService initBpmDimensionService() {
		return context.getBean("bpmDimensionService", BpmDimensionService.class);
	}

	public BpmProcdmsService initBpmProcdmsService() {
		return context.getBean("bpmProcdmsService", BpmProcdmsService.class);
	}

	public BpmMatcherService initBpmMatcherService() {
		return context.getBean("bpmMatcherService", BpmMatcherService.class);
	}

	public BpmProcdefService initBpmProcdefService() {
		return context.getBean("bpmProcdefService", BpmProcdefService.class);
	}

	public BpmTaskService initBpmTaskService() {
		return context.getBean("bpmTaskService", BpmTaskService.class);
	}
	
	public BpmRoleService initBpmRoleService() {
		return context.getBean("bpmRoleService", BpmRoleService.class);
	}
	
	public BpmOwnerService initBpmOwnerService() {
		return context.getBean("bpmOwnerService", BpmOwnerService.class);
	}
	
	public BpmFilterService initBpmFilterService() {
		return context.getBean("bpmFilterService", BpmFilterService.class);
	}
	
	public BpmFilterMatcherService initBpmFilterMatcherService() {
		return context.getBean("bpmFilterMatcherService", BpmFilterMatcherService.class);
	}
	
	public BpmTaskRoleService initBpmTaskRoleService() {
		return context.getBean("bpmTaskRoleService", BpmTaskRoleService.class);
	}
	
}
