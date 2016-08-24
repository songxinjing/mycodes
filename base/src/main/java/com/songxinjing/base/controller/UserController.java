package com.songxinjing.base.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.songxinjing.base.constant.ViewPath;
import com.songxinjing.base.domain.User;
import com.songxinjing.base.plugin.page.PageModel;
import com.songxinjing.base.service.UserService;

/**
 * 树型控制类
 * 
 * @author songxinjing
 * 
 */
@Controller
public class UserController {

	private static Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	UserService userService;

	@RequestMapping(value = { ViewPath.USER_LIST }, method = RequestMethod.GET)
	public String list(Model model, HttpServletRequest request, Integer page) {
		logger.info("进入用户列表页面");

		if (page == null) {
			page = 1;
		}
		
		int total = userService.find().size();

		// 分页代码
		PageModel<User> pageModel = new PageModel<User>();
		pageModel.init(page, total);
		pageModel.setUrl(ViewPath.USER_LIST + ".html");

		List<User> users = userService.findPage(pageModel.getRecFrom(), pageModel.getPageSize());
		pageModel.setRecList(users);

		model.addAttribute("pageModel", pageModel);

		model.addAttribute("menu", "page");
		return ViewPath.USER_LIST;
	}

}
