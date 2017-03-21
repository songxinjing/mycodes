package com.songxinjing.goodfind;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.songxinjing.goodfind.domain.ProdPrice;
import com.songxinjing.goodfind.httpclient.GFHttpClient;
import com.songxinjing.goodfind.selenium.GFSelenium;
import com.songxinjing.goodfind.util.CommUtils;

public class GFStart {

	protected static final Logger logger = LoggerFactory.getLogger(GFStart.class);

	public static void main(String[] args) {

		try {
			// 启动浏览器
			GFSelenium.start();

			// 登录
			GFSelenium.login();

			// 页面刷新
			GFSelenium.refreshStart();

			while (true) {
				ProdPrice longProd = GFHttpClient.getProdPrice("878002");
				ProdPrice shortProd = GFHttpClient.getProdPrice("878003");
				if (longProd == null || shortProd == null) {
					logger.info("无法正确获取价格信息！！！");
					GFSelenium.refreshWait();
					GFSelenium.stop();
					GFSelenium.start();
					GFSelenium.login();
					GFSelenium.refreshNotify();
					continue;
				}
				GFHttpClient.findArbitrage(longProd, shortProd);
				CommUtils.sleep(900);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			GFSelenium.stop();
		}

		GFSelenium.stop();
	}

}
