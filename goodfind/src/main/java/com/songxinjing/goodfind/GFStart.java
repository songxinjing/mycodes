package com.songxinjing.goodfind;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.songxinjing.goodfind.domain.ProdPrice;
import com.songxinjing.goodfind.exception.ConnectionCloseException;
import com.songxinjing.goodfind.httpclient.GFHttpClient;
import com.songxinjing.goodfind.selenium.GFSelenium;
import com.songxinjing.goodfind.util.CommUtils;

public class GFStart {

	protected static final Logger logger = LoggerFactory.getLogger(GFStart.class);

	public static void start() {

		try {
			// 启动浏览器
			GFSelenium.start();

			// 登录
			GFSelenium.login();

			// 处理未成交订单
			GFHttpClient.dealOneSide();

			while (true) {
				try {
					ProdPrice longProd = GFHttpClient.getProdPrice("878002");
					ProdPrice shortProd = GFHttpClient.getProdPrice("878003");
					if (longProd == null || shortProd == null) {
						logger.info("无法正确获取价格信息！！！");
						continue;
					}
					GFHttpClient.findArbitrage(longProd, shortProd);
					CommUtils.sleep(500);

					longProd = GFHttpClient.getProdPrice("878004");
					shortProd = GFHttpClient.getProdPrice("878005");
					if (longProd == null || shortProd == null) {
						logger.info("无法正确获取价格信息！！！");
						continue;
					}
					GFHttpClient.findArbitrage(longProd, shortProd);
					CommUtils.sleep(500);
				} catch (ConnectionCloseException e) {
					logger.info("链接中断！！！");
					GFSelenium.stop();
					GFSelenium.start();
					GFSelenium.login();
					GFHttpClient.dealOneSide();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		start();
	}

}
