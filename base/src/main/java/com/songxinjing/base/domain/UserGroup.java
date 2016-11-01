package com.songxinjing.base.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * 用户组信息表实体类
 * 
 * @author songxinjing
 *
 */
@Entity
public class UserGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户组ID
	 */
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer groupId;

	/**
	 * 用户组名称
	 */
	@Column(length = 16)
	private String groupName;

	/**
	 * 用户组状态：0-正常；1-删除
	 */
	@Column
	private Integer state;

	/**
	 * 成员列表
	 */
	@ManyToMany
	private List<User> members;

	/**
	 * 用户组所属角色列表
	 */
	@ManyToMany(mappedBy = "userGroups", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<Role> roles;

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public List<User> getMembers() {
		return members;
	}

	public void setMembers(List<User> members) {
		this.members = members;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}