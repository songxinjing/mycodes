package com.songxinjing.base.dao;

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

}
