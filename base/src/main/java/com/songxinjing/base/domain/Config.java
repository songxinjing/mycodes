package com.songxinjing.base.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 配置信息表实体类
 * 
 * @author songxinjing
 *
 */
@Entity
public class Config implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 配置项Key
	 */
	@Id
	@Column(length = 32)
	private String key;

	/**
	 * 配置项Value
	 */
	@Column(length = 128)
	private String value;

	/**
	 * 配置项描述
	 */
	@Column(length = 255)
	private String desc;

	/**
	 * 是否激活：0-未激活；1-激活
	 */
	@Column
	private Integer enable;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

}