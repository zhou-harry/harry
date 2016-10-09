package com.harry.service;

import com.harry.entity.User;
import com.harry.util.Result;

public interface UserService {

	/**
	 * 查询用户信息
	 * @param user
	 * @return
	 * @throws Exception
	 */
	User getUser(User user) throws Exception;
	/**
	 * 更新登录信息
	 * @param user
	 * @return
	 * @throws RuntimeException
	 */
	Result updateLoginInfo(User user)throws RuntimeException;
}
