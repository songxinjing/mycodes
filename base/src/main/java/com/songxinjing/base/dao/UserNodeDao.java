package com.songxinjing.base.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.songxinjing.base.dao.base.BaseDao;
import com.songxinjing.base.domain.UserNode;

/**
 * 用户选中节点表Dao类
 * @author songxinjing
 *
 */
@Repository
public class UserNodeDao extends BaseDao<UserNode, Integer> {
	
	/**
	 * 根据用户删除选中节点
	 * 
	 * @param userId 用户ID
	 */
	public void delByUserId(String userId) {
		String hql = "delete from UserNode where userId = ? ";
		this.updOrDel(hql, userId);
	}
	
	/**
	 * 根据用户查询选中节点
	 * 
	 * @param userId 用户ID
	 * @return 选中的节点
	 */
	public List<UserNode> findByUserId(String userId) {
		UserNode temp = new UserNode();
		temp.setUserId(userId);
		return this.find(temp);
	}
	
}
