package com.songxinjing.base.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户组信息表实体类
 * 
 * @author songxinjing
 *
 */
@Entity
@Table(name = "BASE_USER_GROUP")
public class UserGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户组ID
	 */
	@Id
	@Column(name = "GROUP_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer groupId;

	/**
	 * 用户组名称
	 */
	@Column(name = "GROUP_NAME", length = 16)
	private String groupName;

	/**
	 * 用户组状态：0-正常；1-删除
	 */
	@Column(name = "STATE")
	private Integer state;

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

}