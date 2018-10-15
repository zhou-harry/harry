package com.harry.fssc.enums;
/**
 * 切面标记
 * @author harry
 *
 */
public enum AspectType {

	LOGIN("home/login", "登录"),
	FILE_UPLOAD("file/upload","上传业务附件"),
	BPM_DEPLOY("bpm/deploy","部署流程模板"),
	BPM_DEPLOY_DELETE("bpm/deleteDeploy","删除流程模板"),
	BPM_STARTPROCESS("bpm/startProcess","启动流程"),
	BPM_INSTANCES("bpm/procInstances","查询流程实例"),
	BPM_DIMENSION_SAVE("bpmConfig/saveDimension","保存流程维度"),
	USER_LIST("user/userList","查询系统用户"),
	USER_SAVE("user/save","编辑用户"),
	ROLE_SAVE("role/save","编辑菜单权限"),
	ROLE_REMOVE("role/remove","删除菜单权限"),
	REDIS_INFO("redis/info","Redis缓存监控"),
	LOGOUT_FORCE("session/forceLogout","强制注销"),
	LOGOUT("home/logout","注销");

	private String key;
	private String name;

	private AspectType(String key, String name) {
		this.key = key;
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}
	
	
}
