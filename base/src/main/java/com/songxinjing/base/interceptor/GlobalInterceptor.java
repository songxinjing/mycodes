package com.songxinjing.base.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.songxinjing.base.domain.Config;
import com.songxinjing.base.service.ConfigService;

/**
 * 全局拦截器
 * 
 * @author songxinjing
 * 
 */
@Component
public class GlobalInterceptor implements HandlerInterceptor {

	@Autowired
	ConfigService configService;

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelView) throws Exception {
		List<Config> configs = configService.findEnable();
		for (Config c : configs) {
			modelView.addObject(c.getName(), c.getValue());
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return true;
	}
}
