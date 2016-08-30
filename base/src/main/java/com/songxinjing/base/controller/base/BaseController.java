package com.songxinjing.base.controller.base;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.songxinjing.base.domain.User;

public abstract class BaseController {

	protected static Logger logger = LoggerFactory.getLogger(BaseController.class);

	/**
	 * 获取当前登录用户信息
	 * 
	 * @param request
	 * @return
	 */
	public User getLoginUser(HttpServletRequest request) {
		return null;
	}	

	/**
	 * 获取当前登录用户的IP地址
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String getIpAddr(HttpServletRequest request) throws Exception {
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}
	
}
