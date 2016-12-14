package com.songxinjing.base.service;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import com.songxinjing.base.domain.DealRecord;
import com.songxinjing.base.domain.Order;

/**
 * 配置信息服务类
 * 
 * @author songxinjing
 * 
 */
@Service
public class DealService {

	/**
	 * 买盘
	 */
	private TreeMap<Integer, Integer> bidMap = new TreeMap<Integer, Integer>();

	/**
	 * 卖盘
	 */
	private TreeMap<Integer, Integer> askMap = new TreeMap<Integer, Integer>();

	/**
	 * 待处理买方订单
	 */
	private TreeMap<Integer, List<Order>> toDealBidOrder = new TreeMap<Integer, List<Order>>();

	/**
	 * 待处理卖方订单
	 */
	private TreeMap<Integer, List<Order>> toDealAskOrder = new TreeMap<Integer, List<Order>>();

	/**
	 * 成交纪录
	 */
	private List<DealRecord> dealRecord = new ArrayList<DealRecord>();

	/**
	 * 当前买方最高出价
	 */
	private int topBid = 0;

	/**
	 * 当前卖方最低出价
	 */
	private int bottomAsk = Integer.MAX_VALUE;

	public TreeMap<Integer, Integer> getBidMap() {
		return bidMap;
	}

	public TreeMap<Integer, Integer> getAskMap() {
		return askMap;
	}

	public void deal(Order order) {
		int notDealQuantity = order.getNotDealQuantity();
		int price = order.getPrice().intValue();
		if (order.getIsBid()) { // 买方订单
			if (price < bottomAsk) { // 小于卖方最低价
				if (toDealBidOrder.containsKey(price)) { // 待处理订单已有同样价格
					toDealBidOrder.get(price).add(order);
				} else {
					List<Order> list = new ArrayList<Order>();
					list.add(order);
					toDealBidOrder.put(price, list);
				}

				if (bidMap.containsKey(price)) { // 买盘已有同价订单
					int oldQuantity = bidMap.get(price);
					int newQuantity = oldQuantity + notDealQuantity;
					bidMap.replace(price, oldQuantity, newQuantity);
				} else {
					bidMap.put(price, notDealQuantity);
				}

				if (price > topBid) { // 更新买方最高出价
					topBid = price;
				}
			} else { // 大于等于卖方最低价，可撮合交易
				List<Order> askOrders = toDealAskOrder.get(bottomAsk);
				int bottomAskQuantity = askMap.get(bottomAsk);
				if (notDealQuantity < bottomAskQuantity) { // 数量小于最低价卖盘数量
					boolean isDone = false;
					List<Order> dealedAskOrders = new ArrayList<Order>();
					int remainBottomAskQuantity = bottomAskQuantity - notDealQuantity;
					for (Order askOrder : askOrders) {
						int askQuantity = askOrder.getNotDealQuantity();
						if (askQuantity <= notDealQuantity) {
							dealedAskOrders.add(askOrder);
							notDealQuantity -= askQuantity;
							order.setNotDealQuantity(notDealQuantity);
							askOrder.setNotDealQuantity(0);
							DealRecord record = new DealRecord(order, askOrder, bottomAsk, askQuantity, true);
							dealRecord.add(record);
							System.out.println("成交：" + record);
							if (notDealQuantity == 0) {
								isDone = true;
							}
						} else {
							int remainQuantity = askQuantity - notDealQuantity;
							order.setNotDealQuantity(0);
							askOrder.setNotDealQuantity(remainQuantity);
							DealRecord record = new DealRecord(order, askOrder, bottomAsk, notDealQuantity, true);
							dealRecord.add(record);
							System.out.println("成交：" + record);
							isDone = true;
						}

						if (isDone) {
							askMap.replace(bottomAsk, bottomAskQuantity, remainBottomAskQuantity);
							askOrders.removeAll(dealedAskOrders);
							break; // 当前订单已处理完毕
						}
					}
				} else { // 数量大于最低价卖盘数量，拆单全部撮合，更新卖方最低价，剩余订单递归撮合
					for (Order askOrder : askOrders) {
						int askQuantity = askOrder.getNotDealQuantity();
						notDealQuantity -= askQuantity;
						order.setNotDealQuantity(notDealQuantity);
						askOrder.setNotDealQuantity(0);
						DealRecord record = new DealRecord(order, askOrder, bottomAsk, askQuantity, true);
						dealRecord.add(record);
						System.out.println("成交：" + record);
					}
					askMap.remove(bottomAsk);
					toDealAskOrder.remove(bottomAsk);
					if (askMap.isEmpty()) {
						bottomAsk = Integer.MAX_VALUE;
					} else {
						bottomAsk = askMap.firstKey();
					}
					if (notDealQuantity > 0) {
						deal(order);
					}
				}
			}
		} else { // 卖方订单
			if (price > topBid) { // 大于买方最高价
				if (toDealAskOrder.containsKey(price)) { // 待处理订单已有同样价格
					toDealAskOrder.get(price).add(order);
				} else {
					List<Order> list = new ArrayList<Order>();
					list.add(order);
					toDealAskOrder.put(price, list);
				}

				if (askMap.containsKey(price)) { // 卖盘已有同价订单
					int oldQuantity = askMap.get(price);
					int newQuantity = oldQuantity + notDealQuantity;
					askMap.replace(price, oldQuantity, newQuantity);
				} else {
					askMap.put(price, notDealQuantity);
				}

				if (price < bottomAsk) { // 更新卖方最低出价
					bottomAsk = price;
				}
			} else { // 小于等于买方最高价，可撮合交易
				List<Order> bidOrders = toDealBidOrder.get(topBid);
				int topBidQuantity = bidMap.get(topBid);
				if (notDealQuantity < topBidQuantity) { // 数量小于最低价卖盘数量
					boolean isDone = false;
					List<Order> dealedBidOrders = new ArrayList<Order>();
					int remainTopBidQuantity = topBidQuantity - notDealQuantity;
					for (Order bidOrder : bidOrders) {
						int bidQuantity = bidOrder.getNotDealQuantity();
						if (bidQuantity <= notDealQuantity) {
							dealedBidOrders.add(bidOrder);
							notDealQuantity -= bidQuantity;
							order.setNotDealQuantity(notDealQuantity);
							bidOrder.setNotDealQuantity(0);
							DealRecord record = new DealRecord(bidOrder, order, topBid, bidQuantity, false);
							dealRecord.add(record);
							System.out.println("成交：" + record);
							if (notDealQuantity == 0) {
								isDone = true;
							}
						} else {
							int remainQuantity = bidQuantity - notDealQuantity;
							order.setNotDealQuantity(0);
							bidOrder.setNotDealQuantity(remainQuantity);
							DealRecord record = new DealRecord(bidOrder, order, topBid, notDealQuantity, true);
							dealRecord.add(record);
							System.out.println("成交：" + record);
							isDone = true;
						}

						if (isDone) {
							bidMap.replace(topBid, topBidQuantity, remainTopBidQuantity);
							bidOrders.removeAll(dealedBidOrders);
							break; // 当前订单已处理完毕
						}
					}
				} else { // 数量大于最低价卖盘数量，拆单全部撮合，更新卖方最低价，剩余订单递归撮合
					for (Order bidOrder : bidOrders) {
						int bidQuantity = bidOrder.getNotDealQuantity();
						notDealQuantity -= bidQuantity;
						order.setNotDealQuantity(notDealQuantity);
						bidOrder.setNotDealQuantity(0);
						DealRecord record = new DealRecord(bidOrder, order, topBid, bidQuantity, true);
						dealRecord.add(record);
						System.out.println("成交：" + record);
					}
					bidMap.remove(topBid);
					toDealBidOrder.remove(topBid);
					if (bidMap.isEmpty()) {
						topBid = 0;
					} else {
						topBid = bidMap.lastKey();
					}
					if (notDealQuantity > 0) {
						deal(order);
					}
				}
			}

		}
	}

}
