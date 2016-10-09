package com.harry.entity;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

public class User implements RowMapper<User>, Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;//用户id
	private String userName;//名称
	private String password;//密码
	private String descript;//昵称
	private String email;//邮箱
	private String phone;//电话
	private String photo;//头像
	private Timestamp registerTime;//注册时间
	private String registerIp;//注册ip
	private Integer active;//是否有效
	private Timestamp lastLonginTime;
	private String lastLonginIP;
	private Integer loginCount;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Timestamp getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(Timestamp registerTime) {
		this.registerTime = registerTime;
	}
	public String getRegisterIp() {
		return registerIp;
	}
	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}
	public Integer getActive() {
		return active;
	}
	public void setActive(Integer active) {
		this.active = active;
	}
	
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	public Timestamp getLastLonginTime() {
		return lastLonginTime;
	}
	public void setLastLonginTime(Timestamp lastLonginTime) {
		this.lastLonginTime = lastLonginTime;
	}
	public String getLastLonginIP() {
		return lastLonginIP;
	}
	public void setLastLonginIP(String lastLonginIP) {
		this.lastLonginIP = lastLonginIP;
	}
	public Integer getLoginCount() {
		return loginCount;
	}
	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User bean=new User();
		bean.setId(rs.getLong("ID"));
		bean.setUserName(rs.getString("USERNAME"));
		bean.setPassword(rs.getString("PASSWORD"));
		bean.setDescript(rs.getString("DESCRIPT"));
		bean.setEmail(rs.getString("EMAIL"));
		bean.setPhone(rs.getString("PHONE"));
		bean.setRegisterTime(rs.getTimestamp("REGISTERTIME"));
		bean.setRegisterIp(rs.getString("REGISTERIP"));
		bean.setActive(rs.getInt("ACTIVE"));
		bean.setPhoto(rs.getString("PHOTO"));
		bean.setLastLonginIP(rs.getString("LAST_LOGIN_IP"));
		bean.setLastLonginTime(rs.getTimestamp("LAST_LOGIN_TIME"));
		bean.setLoginCount(rs.getInt("LOGIN_COUNT"));
		return bean;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password="
				+ password + ", descript=" + descript + ", email=" + email
				+ ", phone=" + phone + ", photo=" + photo + ", registerTime="
				+ registerTime + ", registerIp=" + registerIp + ", active="
				+ active + ", lastLonginTime=" + lastLonginTime
				+ ", lastLonginIP=" + lastLonginIP + ", loginCount="
				+ loginCount + "]";
	}
	
}
