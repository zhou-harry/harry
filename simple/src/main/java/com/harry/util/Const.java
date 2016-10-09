package com.harry.util;

public class Const {
	public static final String SESSION_USER = "sessionUser";			//session用的用户
	public static final String LOGIN = "/index";				//登录地址
	public static final String NO_INTERCEPTOR_PATH = ".*/((view)|(css)|(font)|(fonts)|(ico)|(img)|(js)|(assets)|(main)|(login)|(logout)|(index)).*";	//不对匹配该值的访问路径拦截（正则）
	public static final int page = 15;
	public static final int minpage = 5;
	public static final int midpage = 10;
}
