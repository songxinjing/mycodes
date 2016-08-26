package com.songxinjing.base.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

/**
 * 树结构节点信息表实体类
 * 
 * @author songxinjing
 *
 */
@Entity
public class TreeNode implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 节点ID
	 */
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer nodeId;

	/**
	 * 节点名称
	 */
	@Column(length = 32)
	private String nodeName;

	/**
	 * 同级排序
	 */
	@Column
	private Integer orderNum;

	/**
	 * 节点状态：0-正常；1-删除
	 */
	@Column
	private Integer state;

	/**
	 * 父节点
	 */
	@ManyToOne
	@JoinColumn
	private TreeNode parent;

	/**
	 * 子节点列表
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@OrderBy("orderNum")
	private List<TreeNode> children;

	/**
	 * 选中该节点用户列表
	 */
	@ManyToMany
	private List<User> selectedUsers;

	public Integer getNodeId() {
		return nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public List<User> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(List<User> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

}