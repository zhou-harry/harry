package com.harry.fssc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harry.fssc.model.UserBehavior;
import com.harry.fssc.repository.BehaviorRepository;

@Component
public class BehaviorService {

	@Autowired
	private BehaviorRepository repository;
	
	public UserBehavior save(UserBehavior behavior) {
		UserBehavior flush = repository.saveAndFlush(behavior);
		return flush;
	}
	
	public List<UserBehavior>recentActivities(String userid, String sessionId){
		return repository.recentActivities(userid,sessionId);
	}
	
	public List<UserBehavior>allActivities(String userid){
		return repository.allActivities(userid);
	}
	
	public List<UserBehavior>recentBehavior(String userid){
		return repository.findByUserId(userid);
	}
}
