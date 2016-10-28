package com.songxinjing.base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.songxinjing.base.dao.RoleDao;
import com.songxinjing.base.domain.Role;
import com.songxinjing.base.service.base.BaseService;

/**
 * 配置信息服务类
 * 
 * @author songxinjing
 * 
 */
@Service
public class RoleService extends BaseService<Role, String>{

	@Autowired
	public void setSuperDao(RoleDao roleDao) {
		super.setDao(roleDao);
	}

}
