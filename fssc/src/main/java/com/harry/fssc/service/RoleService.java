package com.harry.fssc.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.harry.fssc.model.Role;
import com.harry.fssc.model.RoleMenu;
import com.harry.fssc.repository.RoleMenuRepository;
import com.harry.fssc.repository.RoleRepository;

@Component
public class RoleService {
	private static Logger logger = LoggerFactory.getLogger(RoleService.class);

	@Autowired
	private RoleRepository repository;
	@Autowired
	private RoleMenuRepository repositoryRoleMenu;

	public Role saveRole(Role role) {
		Role flush = repository.saveAndFlush(role);
		return flush;
	}
	
	public RoleMenu saveRoleMenu(RoleMenu roleMenu) {
		RoleMenu flush = repositoryRoleMenu.saveAndFlush(roleMenu);
		return flush;
	}
	
	public void deleteRoleMenu(RoleMenu roleMenu) {
		repositoryRoleMenu.deleteByRoleIdAndMenuId(roleMenu.getRoleId(), roleMenu.getMenuId());;
	}
	
	public void deleteRole(String roleId) {
		repository.deleteByRoleId(roleId);
	}
	
	public Role findRole(String roleid) {
		return repository.findByRoleId(roleid);
	}

	public List<Role> findAll() {
		return repository.findAll(Sort.by(Order.desc("created")));
	}
	
	public List<Role> findByUser(String userid) {
		return repository.findByUser(userid);
	}
}
