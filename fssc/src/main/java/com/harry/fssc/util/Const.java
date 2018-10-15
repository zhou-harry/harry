package com.harry.fssc.util;

public class Const {

	public static final String SEQ_ROLEID = "SEQ_ROLEID";
	public static final String SEQ_FILTERID = "SEQ_FILTERID";
	public static final String SEQ_BPM_ROLEID = "SEQ_BPM_ROLEID";
	public static final String SEQ_BPM_TASKID = "SEQ_BPM_TASKID";

	public static final long GLOBAL_SESSION_TIMEOUT = 1800 * 1000;// session 在redis过期时间,单位毫秒
	public static final String REDIS_SHIRO_CACHE = "harry-shiro-cache:";
	public static final String ACTIVE_SESSION_CACHE = "shiro-activeSessionCache";
	public static final String REDIS_SESSION_PREFIX = "harry-shiro-session:";
	public static final String REDIS_SESSION_COUNT = "harry-shiro-session-count:";
	public static final int MAX_RETRY_COUNT=5;//登录失败尝试次数
	public static final String PASSWORD_RETYR_CACHE="passwordRetryCache";//登录计数器
	public static final String COOKIE_REMENBERME="cookie.rememberMe";
	public static final int COOKIE_REMENBERME_MAX_AGE=86400;//记住我cookie生效时间1天 ,单位秒,-1 表示浏览器关闭时失效此 Cookie；
	
	
	public static final String FILE_PREVIEW="../../../fssc/file/preview?id=";
	
	public static final int MID_PRIORITY=50;
	
}
