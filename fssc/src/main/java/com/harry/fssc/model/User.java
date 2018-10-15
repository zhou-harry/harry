package com.harry.fssc.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.harry.fssc.util.Const;
@Entity
@Table(schema = "DBUSER", name = "T_USER")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER")
	@SequenceGenerator(sequenceName = "T_USER_SEQ", allocationSize = 1, name = "SEQ_USER")
	private Long id;
	@Column(name="USER_ID",unique = true)
	private String userId;
	private String password;
	private String name;
	private String salt;// 加密密码
	@Column(name="STATE")
    private Integer state;
    private String email;
    private String phone;
    private String photo;
    @Column(name="COMPANY")
    private String company;
    @Column(name="CREATED_DATE",insertable = false,updatable = false,columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdDate;
    @Column(name="LAST_UPDATE")
    private Timestamp lastUpdate;
    @Transient
    private List<Role> roles;
	
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
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
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public Timestamp getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	/**  
     * 加密码.  
     *   
     * @return  
     */  
    public String getCredentialsSalt() {  
        return this.userId + this.salt;  
    }
	public User() {
		super();
	}
	
	public User(String userId) {
		super();
		this.userId = userId;
	}
	/**
	 * @param userId
	 * @param password
	 * @param name
	 */
	public User(String userId, String password, String name) {
		this.userId = userId;
		this.password = password;
		this.name = name;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId  + ", name=" + name  + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((salt == null) ? 0 : salt.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (salt == null) {
			if (other.salt != null)
				return false;
		} else if (!salt.equals(other.salt))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	public Map<Object, Object> response(){
		Map<Object, Object>result=new HashMap<>();
		
		result.put("userId", this.userId);
		result.put("name", this.name);
		result.put("photo", this.photo);
		result.put("email", this.email);
		result.put("preview", Const.FILE_PREVIEW+this.photo);
		
		return result;
	}
}
