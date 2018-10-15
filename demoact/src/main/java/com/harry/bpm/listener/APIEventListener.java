package com.harry.bpm.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiVariableEvent;
import org.activiti.engine.delegate.event.impl.ActivitiActivityEventImpl;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.delegate.event.impl.ActivitiProcessStartedEventImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

public class APIEventListener implements ActivitiEventListener {

	public void onEvent(ActivitiEvent event) {

		// TODO Auto-generated method stub
		System.out.println("监听进来..." + event.getType());
		switch (event.getType()) {
		case VARIABLE_CREATED:
			ActivitiVariableEvent variableEvent = (ActivitiVariableEvent) event;

			System.out.println("Event: " + event.getType() + " " + variableEvent.getVariableName() + " ("
					+ variableEvent.getVariableType().getTypeName() + ") = " + variableEvent.getVariableValue());
			break;
		case VARIABLE_DELETED:
			System.out.println("Event: " + event.getType());
			break;
		case VARIABLE_UPDATED:
			System.out.println("Event: " + event.getType());
			break;
		case PROCESS_STARTED:
			ActivitiProcessStartedEventImpl startedEvent = (ActivitiProcessStartedEventImpl) event;
			ExecutionEntity entity2 = (ExecutionEntity) startedEvent.getEntity();
			System.out.println("Event: " + entity2.getName());
			break;
		case ACTIVITY_STARTED:
			ActivitiActivityEventImpl activityEvent = (ActivitiActivityEventImpl) event;
			System.out.println("Event: " + activityEvent.getType());
			break;
		case ACTIVITY_COMPLETED:
			System.out.println("Event: " + event.getType());
			break;
		case TASK_ASSIGNED:
			ActivitiEntityEventImpl entityEvent = (ActivitiEntityEventImpl) event;
			TaskEntity entity = (TaskEntity) entityEvent.getEntity();
			System.out.println("监听任务进来..." + entity.getName());
			break;
		case TASK_CREATED:
			ActivitiEntityEventImpl entityEvent2 = (ActivitiEntityEventImpl) event;
			TaskEntity entity3 = (TaskEntity) entityEvent2.getEntity();
			System.out.println("监听任务进来..." + entity3.getName());
			break;
		}

	}

	public boolean isFailOnException() {
		// TODO Auto-generated method stub
		return false;
	}

}
