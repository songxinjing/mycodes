package com.songxinjing.base.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * 权限信息表实体类
 * 
 * @author songxinjing
 *
 */
@Entity
public class Privilege implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 权限码
	 */
	@Id
	@Column(length = 8)
	private String code;

	/**
	 * 权限名称
	 */
	@Column(length = 64)
	private String name;

	/**
	 * 权限URL
	 */
	@Column(length = 128)
	private String url;

	/**
	 * 权限描述
	 */
	@Column(length = 255)
	private String descp;

	/**
	 * 是否激活：0-未激活；1-激活
	 */
	@Column
	private Boolean enable;

	/**
	 * 权限所属角色列表
	 */
	@ManyToMany(mappedBy = "privileges")
	private List<Role> roles;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescp() {
		return descp;
	}

	public void setDescp(String descp) {
		this.descp = descp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	/**
	 * 判断权限是否相等
	 * 
	 * @param priv
	 * @return
	 */
	public boolean equals(Object obj) {
		if (this == obj) {// 如果引用同一个对象直接返回true
			return true;
		}
		if (obj == null) { // 如果obj为null，直接返回false
			return false;
		}
		if (getClass() != obj.getClass()) {// 如果不属于同一个类的话直接返回false
			return false;
		}
		Privilege priv = (Privilege) obj;
		if (priv.code.equals(this.code)) {
			return true;
		}
		return false;
	}

}