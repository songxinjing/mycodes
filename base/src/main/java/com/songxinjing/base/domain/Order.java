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
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 订单ID
	 */
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderId;

	/**
	 * 订单产品
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	private Product product;

	/**
	 * 订单所有者
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	private User owner;

	/**
	 * 订单价格
	 */
	@Column
	private Integer price;

	/**
	 * 订单数量
	 */
	@Column
	private Integer quantity;

	/**
	 * 未成交数量
	 */
	@Column
	private Integer notDealQuantity;

	/**
	 * 订单方向：true-买，false-卖
	 */
	@Column
	private Boolean isBid;

	/**
	 * 订单时间
	 */
	@Column
	private Timestamp orderTm;

	public Order() {
	}

	/**
	 * 构造方法
	 * 
	 * @param product
	 *            产品
	 * @param owner
	 *            所有人
	 * @param price
	 *            报价
	 * @param quantity
	 *            数量
	 * @param isBid
	 *            方向：true-买，false-卖
	 */
	public Order(Product product, User owner, Integer price, Integer quantity, Boolean isBid) {
		this.product = product;
		this.owner = owner;
		this.price = price;
		this.quantity = quantity;
		this.notDealQuantity = quantity;
		this.isBid = isBid;
		this.orderTm = new Timestamp(System.currentTimeMillis());
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
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

	public Integer getNotDealQuantity() {
		return notDealQuantity;
	}

	public void setNotDealQuantity(Integer notDealQuantity) {
		this.notDealQuantity = notDealQuantity;
	}

	public Boolean getIsBid() {
		return isBid;
	}

	public void setIsBid(Boolean isBid) {
		this.isBid = isBid;
	}

	public Timestamp getOrderTm() {
		return orderTm;
	}

	public void setOrderTm(Timestamp orderTm) {
		this.orderTm = orderTm;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", product=" + product + ", owner=" + owner + ", price=" + price
				+ ", quantity=" + quantity + ", notDealQuantity=" + notDealQuantity + ", isBid=" + isBid + ", orderTm="
				+ orderTm + "]";
	}

}