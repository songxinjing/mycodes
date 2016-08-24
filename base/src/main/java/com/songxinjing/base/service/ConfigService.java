package com.songxinjing.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.songxinjing.base.constant.DataDic;
import com.songxinjing.base.dao.ConfigDao;
import com.songxinjing.base.domain.Config;

/**
 * 配置信息服务类
 * @author songxinjing
 * 
 */
@Service
public class ConfigService {

	@Autowired
	ConfigDao configDao;
	
	/**
	 * 获取指定Key的配置信息
	 * @param key 指定的key
	 * @return
	 */
	public Config find(String key) {
		return configDao.findByPK(key);
	}
	
	/**
	 * 获取生效配置信息
	 * @return
	 */
	public List<Config> findEnable() {		
		Config example = new Config();
		example.setEnable(DataDic.CONFIG_ENABLE);
		return configDao.find(example);
	}
	
	/**
	 * 新增一个配置
	 * @return
	 */
	public String save(Config entity) {
		return (String) configDao.save(entity);
	}
	
	/**
	 * 修改一个配置
	 * @return
	 */
	public void update(Config entity) {
		configDao.update(entity);
	}
	
	/**
	 * 删除一个配置
	 * @return
	 */
	public void delete(String key) {
		configDao.delete(key);
	}
	
	/**
	 * 获取无效的配置信息
	 * @return
	 */
	public List<Config> findNotEnable() {		
		Config example = new Config();
		example.setEnable(DataDic.CONFIG_NOT_ENABLE);
		return configDao.find(example);
	}

}
