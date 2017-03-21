package com.songxinjing.goodfind.http;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.songxinjing.goodfind.selenium.GFSelenium;

public class GFHttpClientTest {

	protected static final Logger logger = LoggerFactory.getLogger(GFHttpClientTest.class);

	@Test
	public void test() {

		try {
			// 启动浏览器
			GFSelenium.start();
			// 登录
			GFSelenium.login();
			// 双向下单
			//GFHttpClient.doubleOrder();

		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 退出
			// GFSelenium.stop();
		} // 退出 GFSelenium.stop();

		/*
		 * try { BufferedImage veriImg = ImageUtils.cleanImage2(ImageIO.read(new
		 * File("logs/1489896971187-old.png"))); Tesseract instance = new
		 * Tesseract(); String veriCodeValue =
		 * instance.doOCR(veriImg).trim().replaceAll("\\s*", "");
		 * logger.info("veriCodeValue: " + veriCodeValue); } catch (IOException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (TesseractException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

	}

}
