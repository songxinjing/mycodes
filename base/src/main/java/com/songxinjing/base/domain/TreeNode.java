package com.songxinjing.base.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 树结构节点信息表实体类
 * 
 * @author songxinjing
 *
 */
@Entity
@Table(name = "BASE_TREE_NODE")
public class TreeNode implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 节点ID
	 */
	@Id
	@Column(name = "NODE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer nodeId;

	/**
	 * 节点名称
	 */
	@Column(name = "NODE_NAME", length = 32)
	private String nodeName;
	
	/**
	 * 同级排序
	 */
	@Column(name = "ORDER_NUM")
	private Integer orderNum;
	
	/**
	 * 父节点ID
	 */
	@Column(name = "PARENT_ID")
	private Integer parentId;

	/**
	 * 用户组状态：0-正常；1-删除
	 */
	@Column(name = "STATE")
	private Integer state;

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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}