package com.harry.fssc.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.harry.fssc.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByRoleId(String roleid);
	
	@Modifying(clearAutomatically = true)
	@Transactional
//    @Query(value = "delete r from Role r where r.roleId=?1")
    void deleteByRoleId(String roleId);
	
	@Query(value = "select r.* from T_ROLE r left join T_ROLE_USER u on r.ROLEID=u.ROLEID where u.USERID=?1", nativeQuery = true)
	List<Role> findByUser(String userid);
}
