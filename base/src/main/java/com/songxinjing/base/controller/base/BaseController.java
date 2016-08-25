package com.songxinjing.base.controller.base;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
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
	 * 为response提供Json格式的返回数据
	 * 
	 * @param obj
	 *            任何对象
	 * @return void
	 */
	public void writeResponse(Object obj, HttpServletResponse response) {
		try {
			response.setContentType("text/json;charset=utf-8");

			String str = JSON.toJSONString(obj);

			PrintWriter writer;
			writer = response.getWriter();
			writer.write(str);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 为response提供String数据的返回，字符集为utf-8
	 * 
	 * @param str
	 *            字符串
	 * @return void
	 */
	public void writeResponseStr(String str, HttpServletResponse response) {

		writeResponseStr(str, response, "utf-8");
	}

	/**
	 * 为response提供String数据的返回
	 * 
	 * @param str
	 *            字符串
	 * @param encoding
	 *            字符编码
	 */
	public void writeResponseStr(String str, HttpServletResponse response, String encoding) {
		try {
			response.setContentType("text/html;charset=" + encoding);

			PrintWriter writer;
			writer = response.getWriter();
			writer.write(str);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	/**
	 * 获取请求参数中所有的信息
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);

				// 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				if (null == res.get(en) || "".equals(res.get(en))) {
					res.remove(en);
				}
			}
		}
		return res;
	}
}
