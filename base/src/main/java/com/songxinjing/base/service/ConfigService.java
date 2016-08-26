package com.songxinjing.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.songxinjing.base.constant.DataDic;
import com.songxinjing.base.dao.ConfigDao;
import com.songxinjing.base.domain.Config;
import com.songxinjing.base.service.base.BaseService;

/**
 * 配置信息服务类
 * @author songxinjing
 * 
 */
@Service
public class ConfigService extends BaseService<Config, String>{

	@Autowired
	public void setSuperDao(ConfigDao configDao) {
		super.setDao(configDao);
	}
	
	/**
	 * 获取生效配置信息
	 * @return
	 */
	public List<Config> findEnable() {		
		Config example = new Config();
		example.setEnable(DataDic.CONFIG_ENABLE);
		return find(example);
	}
	
	/**
	 * 获取无效的配置信息
	 * @return
	 */
	public List<Config> findNotEnable() {		
		Config example = new Config();
		example.setEnable(DataDic.CONFIG_NOT_ENABLE);
		return find(example);
	}

}
