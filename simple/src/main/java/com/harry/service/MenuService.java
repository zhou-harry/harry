package com.harry.service;

import java.util.List;

import com.harry.entity.Menu;

public interface MenuService {

	/**
	 * 根据父菜单id加载菜单信息
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	List<Menu> getMenuByParent(long userId,String parentId) throws Exception;
}
