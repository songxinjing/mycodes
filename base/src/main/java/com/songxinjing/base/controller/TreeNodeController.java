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
import org.springframework.web.bind.annotation.ResponseBody;

import com.songxinjing.base.constant.ViewPath;
import com.songxinjing.base.form.TreeNodeForm;
import com.songxinjing.base.service.TreeNodeService;

/**
 * 树型控制类
 * 
 * @author songxinjing
 * 
 */
@Controller
public class TreeNodeController {

	private static Logger logger = LogManager.getLogger(TreeNodeController.class);

	@Autowired
	TreeNodeService treeNodeService;

	@RequestMapping(value = { ViewPath.TREE_INDEX }, method = RequestMethod.GET)
	public String index(Model model, HttpServletRequest request) {
		logger.info("进入Tree页面");
		return ViewPath.TREE_INDEX;
	}

	@RequestMapping(value = { ViewPath.TREE_DATA }, method = RequestMethod.GET)
	@ResponseBody
	public List<TreeNodeForm> data(Model model, HttpServletRequest request, Integer key) {
		logger.info("获取Tree的数据");
		if (key == null) {
			key = 0;
		}
		return treeNodeService.findChildrenForm(key, false);
	}
	
	@RequestMapping(value = { ViewPath.TREE_EDIT }, method = RequestMethod.GET)
	public String edit(Model model, HttpServletRequest request) {
		logger.info("进入Tree编辑页面");
		return ViewPath.TREE_EDIT;
	}

}
