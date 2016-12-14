package com.songxinjing.base.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 产品信息表实体类
 * 
 * @author songxinjing
 *
 */
@Entity
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 产品ID
	 */
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer prodId;

	/**
	 * 产品名称
	 */
	@Column(length = 32)
	private String name;

	/**
	 * 产品描述
	 */
	@Column(length = 255)
	private String descp;

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescp() {
		return descp;
	}

	public void setDescp(String descp) {
		this.descp = descp;
	}
}