package com.songxinjing.goodfind;

import java.util.Date;

import com.songxinjing.goodfind.domain.ProdPrice;
import com.songxinjing.goodfind.httpclient.GFHttpClient;
import com.songxinjing.goodfind.selenium.GFSelenium;
import com.songxinjing.goodfind.util.CommUtils;

public class GFStart {

	public static void main(String[] args) {

		try {
			// 启动浏览器
			GFSelenium.start();

			// 登录
			GFSelenium.login();

			while (true) {
				ProdPrice longProd = GFHttpClient.getProdPrice("878002");
				ProdPrice shortProd = GFHttpClient.getProdPrice("878003");
				if (longProd == null || shortProd == null) {
					Date now = new Date();
					System.out.println(CommUtils.format.format(now) + " 无法正确获取价格信息！！！");
					GFSelenium.stop();
					GFSelenium.start();
					GFSelenium.login();
					continue;
				}
				GFHttpClient.findArbitrage(longProd, shortProd);
				CommUtils.sleep(400);

				longProd = GFHttpClient.getProdPrice("878004");
				shortProd = GFHttpClient.getProdPrice("878005");
				if (longProd == null || shortProd == null) {
					Date now = new Date();
					System.out.println(CommUtils.format.format(now) + " 无法正确获取价格信息！！！");
					GFSelenium.stop();
					GFSelenium.start();
					GFSelenium.login();
					continue;
				}
				GFHttpClient.findArbitrage(longProd, shortProd);
				CommUtils.sleep(400);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			GFSelenium.stop();
		}

		GFSelenium.stop();
	}

}
