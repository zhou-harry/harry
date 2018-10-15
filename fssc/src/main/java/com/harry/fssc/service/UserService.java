package com.harry.fssc.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import com.harry.fssc.model.RoleUser;
import com.harry.fssc.model.User;
import com.harry.fssc.repository.RoleUserRepository;
import com.harry.fssc.repository.UserRepository;
import com.harry.fssc.util.RedisUtil;

@Component
public class UserService {
	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private RedisUtil util;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleUserRepository roleUserRepository;

	/**
	 * 登录查询(空值不缓存)
	 * @param loginId
	 * @return
	 */
	@Cacheable(value = "findLoginUser", keyGenerator = "harryKeyGenerator",unless="#result == null or #result.state ne 1")
	public User findLoginUser(String loginId) {
		System.out.println("没有从缓存取数据...");
		User loginUser = userRepository.findByUserId(loginId);
		return loginUser;
	}
	/**
	 * 根据用户id查询用户
	 * @param userId
	 * @return
	 */
	public User findUserById(String userId) {
		User user = userRepository.findByUserId(userId);
		return user;
	}
	/**
	 * 单独查询用户图像id
	 * @param userId
	 * @return
	 */
	public String findPhotoById(String userId) {
		return userRepository.findPhotoByUserId(userId);
	}
	/**
	 * 新建用户,检查userid是否重复
	 * @param userid
	 * @return
	 */
	public boolean existsByUserId(String userid) {
		//The ExampleMatcher is immutable and can be static I think
		ExampleMatcher NAME_MATCHER = ExampleMatcher.matching()
		            .withMatcher("userId", GenericPropertyMatchers.ignoreCase());
		Example<User> example = Example.<User>of(new User(userid), NAME_MATCHER);
		return userRepository.exists(example);
	}

	/**
	 * 保存用户
	 * @param user
	 * @return
	 */
	public User saveUser(User user) {
		User flush = userRepository.saveAndFlush(user);
		return flush;
	}
	
	/**
	 * 保存用户权限
	 * @param user
	 * @return
	 */
	public void saveRoleUser(String userId,List<RoleUser> entities) {
		roleUserRepository.deleteByUserId(userId);
		roleUserRepository.saveAll(entities);//.saveAndFlush(roleUser);
	}
	/**
	 * 查询系统中所有用户
	 * @return
	 */
	public List<User>findAll(){
		return userRepository.findAll(Sort.by(Order.desc("createdDate")));
	}
	/**
	 * 根据状态查询用户
	 * @param state
	 * @return
	 */
	public List<User>findByState(Integer state){
		return userRepository.findByStateOrderByCreatedDate(state);
	}

	public User currentUser(String key) {
		Object object = util.get(key);
		if (null == object) {
			return null;
		}
		return (User) object;
	}
}
