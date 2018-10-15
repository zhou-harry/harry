package com.harry.fssc.util;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.harry.fssc.model.User;

/**
 * 密码加密
 * 
 * @author harry
 *
 */
public class PasswordHelper {
	
	private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
	// 散列算法:这里使用MD5算法;
	public static final String algorithm = "md5";
	// 散列的次数，比如散列两次，相当于 md5(md5(""));
	public static final int hashIterations = 2;

	public void encryptPassword(User user) {

		user.setSalt(randomNumberGenerator.nextBytes().toHex());

		String newPassword = new SimpleHash(algorithm, user.getPassword(),
				ByteSource.Util.bytes(user.getCredentialsSalt()), hashIterations).toHex();

		user.setPassword(newPassword);
	}
}
