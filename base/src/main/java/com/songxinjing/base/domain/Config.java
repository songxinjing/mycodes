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
	private String name;

	/**
	 * 配置项Value
	 */
	@Column(length = 128)
	private String value;

	/**
	 * 配置项描述
	 */
	@Column(length = 255)
	private String descp;

	/**
	 * 是否激活：0-未激活；1-激活
	 */
	@Column
	private Boolean enable;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescp() {
		return descp;
	}

	public void setDescp(String descp) {
		this.descp = descp;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

}