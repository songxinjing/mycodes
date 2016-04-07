package com.songxinjing.base.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigPropertyUtil {

	protected static final Logger logger = LogManager.getLogger(ConfigPropertyUtil.class);

	/**
	 * 读取properties文件，并根据key值获取其对应的value值
	 * 
	 * @param propFile
	 *            properties文件路径，相对于class根目录，例如：/config.properties
	 * @param key
	 * @return value 返回key对应的value，如果没有则返回null
	 * @throws IOException
	 */
	public static String getValue(String propFile, String key) {
		Properties props = new Properties();
		String value = null;
		try {
			InputStream ips = ConfigPropertyUtil.class.getResourceAsStream(propFile);
			props.load(ips);
			ips.close();
			value = props.getProperty(key);
		} catch (FileNotFoundException e) {
			logger.error("系统找不到指定的文件!", e);
		} catch (IOException e) {
			logger.error("文件操作有误!", e);
		}
		return value;
	}

}
