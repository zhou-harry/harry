package com.harry.fssc.model.redis;

public class RedisInfo {

	
	private String port;
	
	public static void main(String[] args) {
		String s="192.168.234.128:7003.role";
		String k = s.substring(s.indexOf(":")+1);
		String k2 = s.substring(s.lastIndexOf(".")+1);
		System.out.println(k);
	}
}
