package com.harry.fssc.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.harry.fssc.config.ShiroConfig;
import com.harry.fssc.model.User;
import com.harry.fssc.service.UserService;
import com.harry.fssc.util.UserState;

/**
 * 用户身份校验核心类
 * 
 * @author harry
 *
 */
public class UserRealm extends AuthorizingRealm {
	private static Logger logger = LoggerFactory.getLogger(UserRealm.class);

	@Autowired
	@Lazy
	private UserService userService;

	/**
	 * 即权限验证，验证某个已认证的用户是否拥有某个权限；
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String username = (String) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		// authorizationInfo.setRoles(userService.findRoles(username));
		// authorizationInfo.setStringPermissions(userService.findPermissions(username));
		
//		在shiro配置文件中添加了filterChainDefinitionMap.put(“/add”, “perms[权限添加]”);
//		就说明访问/add这个链接必须要有“权限添加”这个权限才可以访问，
//		如果在shiro配置文件中添加了filterChainDefinitionMap.put(“/add”, “roles[100002]，perms[权限添加]”);
//		就说明访问/add这个链接必须要有“权限添加”这个权限和具有“100002”这个角色才可以访问。
		return authorizationInfo;
	}

	/**
	 * 身份认证/登录，验证用户是不是拥有相应的身份；
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String userId = (String) token.getPrincipal();
		User user = userService.findLoginUser(userId);
		if (user == null) {
			throw new UnknownAccountException();// 没找到帐号
		}
		if (UserState.invalid.getStatus().equals(user.getState())) {
			throw new DisabledAccountException(user.getUserId() + ",帐号未激活");
		}
		if (UserState.locked.getStatus().equals(user.getState())) {
			throw new LockedAccountException(user.getUserId() + ",帐号被锁定"); // 帐号锁定
		}
		// 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(),
				ByteSource.Util.bytes(user.getCredentialsSalt()), getName());
		return authenticationInfo;
	}

}
