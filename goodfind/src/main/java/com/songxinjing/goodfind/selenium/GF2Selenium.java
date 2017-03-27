package com.songxinjing.goodfind.selenium;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.songxinjing.goodfind.util.CommUtils;

public class GF2Selenium {

	protected static final Logger logger = LoggerFactory.getLogger(GF2Selenium.class);

	private static RemoteWebDriver driver;

	public static void start() throws MalformedURLException {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("user-data-dir=/Users/songxinjing/Library/Application Support/Google/Chrome/Default");
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		driver = new RemoteWebDriver(new URL("http://localhost:9515"), DesiredCapabilities.chrome());
		driver.manage().window().setPosition(new Point(0, 0));
		driver.manage().window().setSize(new Dimension(1200, 720));
	}

	public static boolean login() {
		driver.get("http://hippo.gf.com.cn/");
		driver.findElementByXPath("/html/body/div[3]/div/button").click();
		driver.findElementByXPath("/html/body/div[3]/div/div[1]/a").click();
		
		
		CommUtils.sleep(5000);
		//WebElement user = driver.findElementByXPath("/html/body/div[4]/div/div[2]/div/div/form/div[1]/input");
		//WebElement pwd = driver.findElementByXPath("/html/body/div[4]/div/div[2]/div/div/form/div[2]/input");
		//WebElement button = driver.findElementByXPath("/html/body/div[4]/div/div[2]/div/div/form/div[4]/button[1]");
		//user.sendKeys("020109004956");
		//pwd.sendKeys("940421");
		//button.click();
		return true;
	}

	public static void main(String[] args) {
		try {
			GF2Selenium.start();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		GF2Selenium.login();
	}

}
