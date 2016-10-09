package com.harry.service.impl;

import java.sql.Timestamp;

import oracle.sql.TIMESTAMP;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.harry.entity.User;
import com.harry.jdbc.BaseDao;
import com.harry.service.UserService;
import com.harry.util.DES;
import com.harry.util.Result;

@Service("userService")
public class UserServiceImpl extends BaseDao implements UserService {

	public User getUser(User user) throws Exception {
		try {
			String sql = "select u.* from T_USER u where u.USERNAME=? and u.PASSWORD=?";

			JdbcTemplate template = this.getJdbcTemplate();

			String password = DES.encrypt(user.getPassword());

			User currentUser = template.queryForObject(sql, new User(),
					user.getUserName(), password);

			return currentUser;
		} catch (RuntimeException exception) {
			throw exception;
		}
	}

	public Result updateLoginInfo(User user) throws RuntimeException {
		Result result=new Result(true);
		try {
			JdbcTemplate template = this.getJdbcTemplate();

			String sql = "update T_USER set LAST_LOGIN_TIME=?,LAST_LOGIN_IP=?,LOGIN_COUNT=? where ID=?";

			int updateOrder = template.update(sql,
							new Object[] { new Timestamp(System.currentTimeMillis()), user.getLastLonginIP(),
									user.getLoginCount()+1,user.getId() });
			result.setMessage(""+updateOrder);
			result.setStatus(true);
			return result;
		} catch (RuntimeException e) {
			throw e;
		}
	}

}
