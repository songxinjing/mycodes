package com.songxinjing.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.songxinjing.base.dao.UserDao;
import com.songxinjing.base.domain.User;

/**
 * 配置信息服务类
 * 
 * @author songxinjing
 * 
 */
@Service
public class UserService {

	@Autowired
	UserDao userDao;
	
	/**
	 * 获取所有用户
	 */
	public List<User> find(){
		return userDao.find();
	}
	
	/**
	 * 获取所有用户
	 */
	public List<User> findPage(int from, int size){
		 return userDao.findPage(from, size);
	}
}
