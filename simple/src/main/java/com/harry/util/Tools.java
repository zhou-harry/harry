package com.harry.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 说明：常用工具 创建人：FH Q313596790 修改时间：2015年11月24日
 * 
 * @version
 */
public class Tools {

	/**
	 * 随机生成六位数验证码
	 * 
	 * @return
	 */
	public static int getRandomNum() {
		Random r = new Random();
		return r.nextInt(900000) + 100000;// (Math.random()*(999999-100000)+100000)
	}

	/**
	 * 检测字符串是否不为空(null,"","null")
	 * 
	 * @param s
	 * @return 不为空则返回true，否则返回false
	 */
	public static boolean notEmpty(String s) {
		return s != null && s.length() != 0 && !"null".equals(s);
	}
	/**
	 * 检查非空
	 * @param s
	 * @return 不为空则返回true，否则返回false
	 */
	public static boolean notEmpty(Object s) {
		if (null==s) {
			return false;
		}
		if (s instanceof String) {
			return s.toString().length() != 0 && !"null".equals(s);
		}
		return true;
	}

	/**
	 * 检测字符串是否为空(null,"","null")
	 * 
	 * @param s
	 * @return 为空则返回true，不否则返回false
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.length() == 0 || "null".equals(s);
	}

	public static String getDate(Date date) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}

}
