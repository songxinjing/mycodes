package com.songxinjing.base.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.songxinjing.base.constant.ViewPath;

/**
 * 主页控制类
 * 
 * @author songxinjing
 * 
 */
@Controller
public class HomeController {

	private static Logger logger = LogManager.getLogger(HomeController.class);

	@RequestMapping(value = { ViewPath.INDEX }, method = RequestMethod.GET)
	public String home(Model model, HttpServletRequest request) {
		logger.info("进入主页面");
		return ViewPath.INDEX;
	}

	@RequestMapping(value = ViewPath.SYSTEM_ERROR, method = RequestMethod.GET)
	public String error() {
		logger.info("进入报错页面");
		return ViewPath.SYSTEM_ERROR;
	}

	@RequestMapping(value = ViewPath.SYSTEM_404, method = RequestMethod.GET)
	public String notFound() {
		logger.info("进入404页面");
		return ViewPath.SYSTEM_404;
	}
	
}
