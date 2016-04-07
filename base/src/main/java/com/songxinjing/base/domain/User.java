package com.songxinjing.base.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 账户信息表实体类
 * 
 * @author songxinjing
 *
 */
@Entity
@Table(name = "BASE_USER")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@Id
	@Column(name = "USER_ID", length = 16)
	private String userId;

	/**
	 * 邮箱
	 */
	@Column(name = "EMAIL", length = 64)
	private String email;

	/**
	 * 手机号码
	 */
	@Column(name = "PHONE", length = 16)
	private String phone;

	/**
	 * 密码密文
	 */
	@Column(name = "PASSWORD", length = 64)
	private String password;

	/**
	 * 用户状态：0-正常；1-冻结；2-删除
	 */
	@Column(name = "STATE")
	private Integer state;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}