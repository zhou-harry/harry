package com.harry.fssc.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.harry.fssc.model.Role;
import com.harry.fssc.model.RoleMenu;

public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long> {

	@Modifying(clearAutomatically = true)
	@Transactional
    void deleteByRoleIdAndMenuId(String roleId,String menuId);
}
