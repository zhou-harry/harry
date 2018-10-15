package com.harry.fssc.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.harry.fssc.model.RoleUser;

public interface RoleUserRepository extends JpaRepository<RoleUser, Long> {

	@Modifying(clearAutomatically = true)
	@Transactional
    void deleteByUserId(String userId);
}
