package com.songxinjing.goodfind;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.songxinjing.goodfind.domain.ProdPrice;
import com.songxinjing.goodfind.exception.ResponseNullException;
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

			while (true) {
				try {
					GFHttpClient.dealOneSide();
					CommUtils.sleep(200);
					ProdPrice longProd = GFHttpClient.getProdPrice("878002");
					ProdPrice shortProd = GFHttpClient.getProdPrice("878003");
					if (longProd == null || shortProd == null) {
						logger.info("无法正确获取价格信息！！！");
						GFSelenium.stop();
						GFSelenium.start();
						GFSelenium.login();
						continue;
					}
					GFHttpClient.findArbitrage(longProd, shortProd);
					CommUtils.sleep(300);

					longProd = GFHttpClient.getProdPrice("878004");
					shortProd = GFHttpClient.getProdPrice("878005");
					if (longProd == null || shortProd == null) {
						logger.info("无法正确获取价格信息！！！");
						GFSelenium.stop();
						GFSelenium.start();
						GFSelenium.login();
						continue;
					}
					GFHttpClient.findArbitrage(longProd, shortProd);
					CommUtils.sleep(300);
				} catch (ResponseNullException e) {
					e.printStackTrace();
					logger.info("获取的应答报文为空！！！");
					GFSelenium.stop();
					GFSelenium.start();
					GFSelenium.login();
					continue;
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
