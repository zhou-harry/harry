package com.harry.util;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DES {

//	public static void main(String arg[]) throws Exception {
//		//加密
//		String encrypt = encrypt("abcd1234");
//		//解密
//		String decrypt = getSSOTicket(encrypt);
//		System.out.println(encrypt+" : " + decrypt);
//		
//	}

	/**
	 * DES加密=>base64编码=>URLEncoder编码
	 * 
	 * @param message
	 *            明文
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String message) throws Exception {
		/* 生成加密约定的密钥入向量 */
		byte[] key = new byte[8];
		byte[] ivs = new byte[8];
		createKeyIV(key, ivs);
		/* 生成密钥 */
		DESKeySpec desKeySpec = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		/* 生成初始向量 */
		IvParameterSpec iv = new IvParameterSpec(ivs);
		/* 初始化解码器 */
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

		byte[] retByte = null;
		String username = "";
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			retByte = cipher.doFinal(message.getBytes());
			String base64 = new BASE64Encoder().encode(retByte);
			username = URLEncoder.encode(base64);
			return username;
		} catch (Exception e) {
			return username = "error";
		}
	}

	/**
	 * DES解码，该密文经DES加密后再经base64编码
	 * 
	 * @param message
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	public static String decrypt(String message) {
		String username = "";
		/* 生成加密约定的密钥入向量 */
		byte[] key = new byte[8];
		byte[] ivs = new byte[8];
		createKeyIV(key, ivs);
		message=message.replaceAll("\\+", "%2B");
		message = URLDecoder.decode(message);
		try {
		/* base64解码 */
		byte[] bytesrc = new BASE64Decoder().decodeBuffer(message);
		/* 初始化解码器 */
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		/* 生成解码密钥 */
		DESKeySpec desKeySpec = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		/* 生成初始向量 */
		IvParameterSpec iv = new IvParameterSpec(ivs);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
			byte[] retByte = cipher.doFinal(bytesrc);
			username = new String(retByte);
			return username;
		} catch (IOException e) {
			System.out.println("error:decrypt()[" + message + "]");
			e.printStackTrace();
			return username = "error";
		} catch (NoSuchAlgorithmException e) {
			System.out.println("error:decrypt()[" + message + "]");
			e.printStackTrace();
			return username = "error";
		} catch (NoSuchPaddingException e) {
			System.out.println("error:decrypt()[" + message + "]");
			e.printStackTrace();
			return username = "error";
		} catch (InvalidKeyException e) {
			System.out.println("error:decrypt()[" + message + "]");
			e.printStackTrace();
			return username = "error";
		} catch (InvalidKeySpecException e) {
			System.out.println("error:decrypt()[" + message + "]");
			e.printStackTrace();
			return username = "error";
		} catch (InvalidAlgorithmParameterException e) {
			System.out.println("error:decrypt()[" + message + "]");
			e.printStackTrace();
			return username = "error";
		}catch (IllegalBlockSizeException e) {
			System.out.println("error:decrypt()[" + message + "]");
			e.printStackTrace();
			return username = "error";
		} catch (BadPaddingException e) {
			System.out.println("error:decrypt()[" + message + "]");
			e.printStackTrace();
			return username = "error";
		}
	}

	/**
	 * 获取当前日期，并以当前日期的0时0分减去1900-1-1 0:00得到的 秒数作为密钥生成基础，如 2008-6-13 0:00 -
	 * 1900-1-1 0:00时间差换算成为秒
	 * 
	 * @param key
	 *            生成的密钥
	 * @param ivs
	 *            生成的初始向量
	 */
	private static void createKeyIV(byte[] key, byte[] ivs) {
		Calendar today = Calendar.getInstance();
		Calendar grdate = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		
		//带时间验证
//		grdate.set(1900, 0, 1, 0, 0, 0);
//		long scondSub = today.getTimeInMillis() - grdate.getTimeInMillis();
		//不带时间验证
		grdate.set(2013, 0, 1, 0, 0, 0);
		long scondSub =grdate.getTimeInMillis();
		
		scondSub = scondSub / 1000;
		byte[] md5Hash = encrypt2(String.valueOf(scondSub).getBytes());
		System.arraycopy(md5Hash, 0, key, 0, 8);
		System.arraycopy(md5Hash, 8, ivs, 0, 8);
	}

	/**
	 * MD5加密
	 * 
	 * @param obj
	 *            要加密的字符
	 * @return
	 */
	private static byte[] encrypt2(byte[] obj) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(obj);
			return md5.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String getSSOTicket(String ssoticketStr) {
		String value = decrypt(ssoticketStr);
		if ("error".equals(value)) {
			System.out.println("error :"+ssoticketStr);
			return null;
		}
		try {
			String[] val = value.split(":");
			String username = val[0];
			if (val.length==2) {
				String time = val[1];
				Integer ghour=new Integer(time.substring(0, 2));
				Integer gmin=new Integer(time.substring(2, 4));
				Integer gms=(ghour*60*60*1000)+(gmin*60*1000);
				Calendar cal = Calendar.getInstance();
				Integer hout=cal.get(Calendar.HOUR_OF_DAY);
				Integer min=cal.get(Calendar.MINUTE);
				Integer ms=(hout*60*60*1000)+(min*60*1000);
				
				Integer temp = Math.abs(ms - gms);
				
				long result = temp / 1000;
				if (result > 1800) {
					System.out.println("当前用户认证时间已经超出30分钟");
					return null;
				} else if (result < 0) {
					System.out.println("当前用户认证时间不合法");
					return null;
				}
			}
			return username;
		} catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}
		return null;
	}
	
}
