package com.songxinjing.base.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.songxinjing.base.dao.base.BaseDao;
import com.songxinjing.base.domain.TreeNode;

/**
 * 配置信息表Dao类
 * @author songxinjing
 *
 */
@Repository
public class TreeNodeDao extends BaseDao<TreeNode, Integer> {
	
	/**
	 * 获取父节点下的所有子节点（按顺序）
	 * @param parentId 指定的父节点
	 * @return 所有子节点List
	 */
	@SuppressWarnings("unchecked")
	public List<TreeNode> findChildren(int parentId){
		String hql = "from TreeNode where parentId = ? order by orderNum";
		return (List<TreeNode>)this.find(hql, parentId);
	}
	
	/**
	 * 获取当前最大同级排序序号
	 * @param parentId 指定的父节点
	 * @return 当前最大的同级排序序号
	 */
	public int findMaxOrderNum(int parentId){
		String hql = "select max(orderNum) from TreeNode where parentId = ? ";
		Integer num = (Integer)this.find(hql, parentId).get(0);
		if(num == null){
			num = 0;
		}
		return num + 1;
	}
	
	/**
	 * 获取当前节点的上一个同级节点
	 * 
	 * @param node
	 *            当前节点
	 * @return 上一个同级节点，没有则返回null
	 */
	@SuppressWarnings("unchecked")
	public TreeNode findPreNode(TreeNode node) {
		String hql = "from TreeNode where parentId = ? and orderNum < ? order by orderNum desc";
		List<TreeNode> list = (List<TreeNode>) this.find(hql, node.getParentId(), node.getOrderNum());
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 获取当前节点的下一个同级节点
	 * 
	 * @param node
	 *            当前节点
	 * @return 下一个同级节点，没有则返回null
	 */
	@SuppressWarnings("unchecked")
	public TreeNode findNextNode(TreeNode node) {
		String hql = "from TreeNode where parentId = ? and orderNum > ? order by orderNum";
		List<TreeNode> list = (List<TreeNode>) this.find(hql, node.getParentId(), node.getOrderNum());
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

}
