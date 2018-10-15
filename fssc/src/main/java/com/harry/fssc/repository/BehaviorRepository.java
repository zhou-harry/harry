package com.harry.fssc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.harry.fssc.model.UserBehavior;

public interface BehaviorRepository extends JpaRepository<UserBehavior, Long> {

	List<UserBehavior> findByUserId(String userid);

	@Query(value = "select * from T_USER_BEHAVIOR where SESSION_ID=(select SESSION_ID from(select row_number()over (order by DATE_ desc) as rn, SESSION_ID from T_USER_BEHAVIOR where USER_ID=?1 and SESSION_ID<>?2 )where rn =1)order by DATE_", nativeQuery = true)
	List<UserBehavior> recentActivities(String userid, String sessionId);
	
	@Query(value = "select * from T_USER_BEHAVIOR where SESSION_ID in(select SESSION_ID from T_USER_BEHAVIOR where USER_ID=?1)order by DATE_ desc", nativeQuery = true)
	List<UserBehavior> allActivities(String userid);

}
