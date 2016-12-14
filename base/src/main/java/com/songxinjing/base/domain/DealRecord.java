package com.songxinjing.base.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * 产品订单信息表实体类
 * 
 * @author songxinjing
 *
 */
@Entity
public class DealRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 成交纪录ID
	 */
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer recId;

	/**
	 * 买方订单
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	private Order bidOrder;

	/**
	 * 卖方订单
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	private Order askOrder;

	/**
	 * 成交价格
	 */
	@Column
	private Integer price;

	/**
	 * 成交数量
	 */
	@Column
	private Integer quantity;

	/**
	 * 主动方：true-买方主动，false-卖方主动
	 */
	@Column
	private Boolean isBidActive;

	/**
	 * 成交时间
	 */
	@Column
	private Timestamp dealTm;

	public DealRecord() {
	}

	/**
	 * 构造方法
	 * 
	 * @param bidOrder
	 *            买方订单
	 * @param askOrder
	 *            卖方订单
	 * @param price
	 *            成交价格
	 * @param quantity
	 *            成交量
	 * @param isBidActive
	 *            主动方：true-买方主动，false-卖方主动
	 */
	public DealRecord(Order bidOrder, Order askOrder, Integer price, Integer quantity, Boolean isBidActive) {
		this.bidOrder = bidOrder;
		this.askOrder = askOrder;
		this.price = price;
		this.quantity = quantity;
		this.isBidActive = isBidActive;
		this.dealTm = new Timestamp(System.currentTimeMillis());
	}

	public Integer getRecId() {
		return recId;
	}

	public void setRecId(Integer recId) {
		this.recId = recId;
	}

	public Order getBidOrder() {
		return bidOrder;
	}

	public void setBidOrder(Order bidOrder) {
		this.bidOrder = bidOrder;
	}

	public Order getAskOrder() {
		return askOrder;
	}

	public void setAskOrder(Order askOrder) {
		this.askOrder = askOrder;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Boolean getIsBidActive() {
		return isBidActive;
	}

	public void setIsBidActive(Boolean isBidActive) {
		this.isBidActive = isBidActive;
	}

	public Timestamp getDealTm() {
		return dealTm;
	}

	public void setDealTm(Timestamp dealTm) {
		this.dealTm = dealTm;
	}

	@Override
	public String toString() {
		return "DealRecord [recId=" + recId + ", bidOrder=" + bidOrder + ", askOrder=" + askOrder + ", price=" + price
				+ ", quantity=" + quantity + ", isBidActive=" + isBidActive + ", dealTm=" + dealTm + "]";
	}

}