package com.songxinjing.base.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * 账户信息表实体类
 * 
 * @author songxinjing
 *
 */
@Entity
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@Id
	@Column(length = 16)
	private String userId;

	/**
	 * 邮箱
	 */
	@Column(length = 64)
	private String email;

	/**
	 * 手机号码
	 */
	@Column(length = 16)
	private String phone;

	/**
	 * 密码密文
	 */
	@Column(length = 64)
	private String password;

	/**
	 * 用户状态：0-正常；1-冻结；2-删除
	 */
	@Column
	private Byte state;

	/**
	 * 用户所属用户组列表
	 */
	@ManyToMany(mappedBy = "members", fetch = FetchType.EAGER)
	private List<UserGroup> groups;

	/**
	 * 用户所属角色列表
	 */
	@ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
	private List<Role> roles;

	/**
	 * 用户选中节点列表
	 */
	@ManyToMany
	private List<TreeNode> selectedNodes;

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

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	public List<UserGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<UserGroup> groups) {
		this.groups = groups;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<TreeNode> getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(List<TreeNode> selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

}