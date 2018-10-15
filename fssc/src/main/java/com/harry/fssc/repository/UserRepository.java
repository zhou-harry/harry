package com.harry.fssc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.harry.fssc.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserId(String userid);
	
	@Query("select photo from User u where u.userId = ?1")
	String findPhotoByUserId(String userid);
	
	User findByUserIdAndState(String userid,Integer state);
	
	List<User>findByStateOrderByCreatedDate(Integer state);
	
//	@Query("select count(u) from User u where u.userId=id")
//	Long countByUserId(@Param("id")String id);
	
}
