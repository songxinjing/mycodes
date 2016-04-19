package com.songxinjing.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.songxinjing.base.dao.UserNodeDao;
import com.songxinjing.base.domain.UserNode;

/**
 * 配置信息服务类
 * 
 * @author songxinjing
 * 
 */
@Service
public class UserNodeService {

	@Autowired
	UserNodeDao userNodeDao;

	/**
	 * 根据ID获取对象
	 * 
	 * @param id
	 *            节点ID
	 * 
	 * @return TreeNode节点对象
	 */
	public UserNode find(int id) {
		return userNodeDao.findByPK(id);
	}
	
	/**
	 * 根据用户查询选中节点
	 * 
	 * @param userId 用户ID
	 * @return 选中的节点
	 */
	public List<UserNode> findByUserId(String userId) {
		return userNodeDao.findByUserId(userId);
	}
	
	/**
	 * 保存用户选择
	 * 
	 * @param userNode
	 */
	public void saveSelected(int[] keys, String userId) {
		userNodeDao.delByUserId(userId);
		for(int i = 0; i < keys.length; i++){
			UserNode userNode = new UserNode();
			userNode.setNodeId(keys[i]);
			userNode.setUserId(userId);
			userNodeDao.save(userNode);
		}
	}

}
