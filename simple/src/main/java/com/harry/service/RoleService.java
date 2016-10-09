package com.harry.service;

import java.util.List;

import com.harry.entity.Role;
import com.harry.entity.Tree;
import com.harry.util.Result;

public interface RoleService {

	/**
	 * 查询角色信息
	 * @param input
	 * @return
	 */
	List<Role> queryRole(Role input);
	/**
	 * 查询角色信息
	 * @param input
	 * @return
	 */
	List<Role> queryRole(String input);
	/**
	 * 保存/更新角色信息
	 * @param input
	 * @return
	 * @throws RuntimeException
	 */
	Result saveRole(Role input)throws RuntimeException;
	/**
	 * 加载角色树
	 * @return
	 */
	List<Tree> getRoleTree();
}
