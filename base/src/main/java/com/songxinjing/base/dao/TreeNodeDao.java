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

}
