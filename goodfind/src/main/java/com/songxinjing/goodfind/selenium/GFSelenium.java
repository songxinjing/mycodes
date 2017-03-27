package com.songxinjing.goodfind.selenium;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.songxinjing.goodfind.httpclient.GFHttpClient;
import com.songxinjing.goodfind.robot.GFRobot;
import com.songxinjing.goodfind.util.CommUtils;
import com.songxinjing.goodfind.util.ImageUtils;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class GFSelenium {

	private static RemoteWebDriver driver;

	private static Tesseract instance = new Tesseract();
	
	private static DesiredCapabilities capabilities = DesiredCapabilities.chrome();
	
	static {		
		ChromeOptions options = new ChromeOptions();
		options.addArguments("user-data-dir=/Users/songxinjing/Library/Application Support/Google/Chrome/Default");	
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		instance.setDatapath("/usr/local/share");
		instance.setTessVariable("tessedit_char_whitelist", "0123456789abcdefghijklmnopqrstuvwxyz");
	}

	public static void start() throws MalformedURLException {
		System.out.println("启动浏览器...");
		driver = new RemoteWebDriver(new URL("http://localhost:9515"), capabilities);
		driver.manage().window().setPosition(new Point(0, 0));
		driver.manage().window().setSize(new Dimension(960, 700));
	}

	public static void login() {
		boolean isLogin = false;
		while (!isLogin) {
			isLogin = trylogin();
		}
	}

	public static boolean trylogin() {
		driver.get("https://etrade.gf.com.cn/");
		CommUtils.sleep(500);
		WebElement user = driver.findElementById("inputuser");
		user.sendKeys("020109004956");

		boolean continueVeri = true;
		do {
			try {
				GFRobot.entryPwd();
			} catch (AWTException e) {
				e.printStackTrace();
			}
			boolean isVeri = findVeriCode();
			if (isVeri) {
				WebElement loginButton = null;
				try {
					loginButton = driver.findElementByXPath("//*[@id=\"loginContainer\"]/div[1]/a[1]");
				} catch (NoSuchElementException nee) {
					Date now = new Date(); 
					System.out.println(CommUtils.format.format(now) + " 没有找到登录按钮！！！登录成功！！！");
				}

				if (loginButton == null) {
					continueVeri = false;
				} else {
					continueVeri = true;
				}

			} else {
				continueVeri = true;
			}

		} while (continueVeri);

		CommUtils.sleep(3 * 1000);

		GFHttpClient.setjSessionId(getSessionValue("JSESSIONID"));
		GFHttpClient.setDseSessionId(getSessionValue("dse_sessionId"));

		return true;
	}

	public static boolean findVeriCode() {
		byte[] imgBytes = driver.getScreenshotAs(OutputType.BYTES);
		WebElement veriDiv = driver.findElement(By.xpath("//*[@id=\"loginContainer\"]/div[1]/div[5]/div"));
		Point location = veriDiv.getLocation();
		Dimension size = veriDiv.getSize();
		try {
			BufferedImage allImg = ImageIO.read(new ByteArrayInputStream(imgBytes));
			BufferedImage veriImg = allImg.getSubimage(location.getX() * 2, location.getY() * 2, size.getWidth() * 2,
					size.getHeight() * 2);
			veriImg = ImageUtils.cleanImage(veriImg);
			String veriCodeValue = instance.doOCR(veriImg).trim().replaceAll("\\s*", "");
			System.out.println("veriCodeValue: " + veriCodeValue);
			WebElement veriCode = driver.findElementById("verificationCode");
			WebElement loginButton = driver.findElementByXPath("//*[@id=\"loginContainer\"]/div[1]/a[1]");
			if (veriCodeValue.length() == 5 && CommUtils.checkString(veriCodeValue)) {
				CommUtils.sleep(500);
				veriCode.clear();
				veriCode.sendKeys(veriCodeValue);
				loginButton.click();
				CommUtils.sleep(2000);
				return true;
			} else {
				System.out.println("验证码获取失败，重新登录！！！");
				CommUtils.sleep(500);
				veriCode.clear();
				veriCode.sendKeys("12345");
				loginButton.click();
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void stop() {
		driver.quit();
	}

	public static String getSessionValue(String sessionId) {
		for (Cookie cookie : driver.manage().getCookies()) {
			if (sessionId.equals(cookie.getName())) {
				System.out.println(cookie.getName() + " : " + cookie.getValue());
				return cookie.getValue();
			}
		}
		return null;
	}

}
