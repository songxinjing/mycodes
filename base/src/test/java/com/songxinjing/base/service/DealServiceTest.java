package com.songxinjing.base.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.songxinjing.base.domain.Order;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class DealServiceTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	DealService dealService;

	@Test
	public void dealTest() throws InterruptedException {
		int times = 0;
		while(times < 100){
			boolean isBid = Math.random() > 0.5;
			int price = 1000;
			int quantity = (int) (Math.random() * 100);
			if(isBid){
				price = 993 + (int) (Math.random() *10);
			} else {
				price = 998 + (int) (Math.random() *10);
			}
			Order order = new Order(null, null, price, quantity, isBid);
			System.out.println("买盘：" + dealService.getBidMap());
			System.out.println("卖盘：" + dealService.getAskMap());
			System.out.println("当前订单：" + order);
			dealService.deal(order);
			Thread.sleep(1000);
			times++;
		}
		
		
	}

}
