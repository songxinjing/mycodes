package com.songxinjing.base.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.songxinjing.base.dao.base.BaseDao;
import com.songxinjing.base.domain.User;

/**
 * 用户信息表Dao类
 * @author songxinjing
 *
 */
@Repository
public class UserDao extends BaseDao<User, String> {
	
	/**
	 * 获取所有用户
	 */
	@SuppressWarnings("unchecked")
	public List<User> findPage(int from, int size){
		String hql = "from User order by userId";
		return (List<User>) findPage(hql, from, size);
	}

}
