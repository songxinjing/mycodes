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

import com.songxinjing.base.constant.DataDick;
import com.songxinjing.base.constant.ViewPath;
import com.songxinjing.base.domain.TreeNode;
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
		model.addAttribute("menu", "tree");
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
		model.addAttribute("menu", "tree");
		return ViewPath.TREE_EDIT;
	}

	@RequestMapping(value = { ViewPath.TREE_SAVE }, method = RequestMethod.POST)
	@ResponseBody
	public boolean save(Model model, HttpServletRequest request, Integer key, String type, String nodeName) {
		logger.info("保存Tree的数据");
		if (type == null || key == null || "".equals(nodeName)) {
			return false;
		}
		if ("A".equals(type)) { // 新增
			TreeNode treeNode = new TreeNode();
			treeNode.setNodeName(nodeName);
			treeNode.setParentId(key);
			treeNode.setOrderNum(treeNodeService.genOrderNum(key));
			treeNode.setState(DataDick.RECODE_NORMAL);
			treeNodeService.save(treeNode);
			return true;
		}
		if ("U".equals(type)) { // 修改
			TreeNode treeNode = treeNodeService.find(key);
			treeNode.setNodeName(nodeName);
			treeNodeService.update(treeNode);
			return true;
		}
		return false;
	}

	@RequestMapping(value = { ViewPath.TREE_DEL }, method = RequestMethod.POST)
	@ResponseBody
	public boolean del(Model model, HttpServletRequest request, Integer key) {
		logger.info("删除节点");
		if (key == null) {
			return false;
		}
		treeNodeService.delete(key);
		return true;
	}
	
	@RequestMapping(value = { ViewPath.TREE_CHILDLIST }, method = RequestMethod.POST)
	@ResponseBody
	public List<TreeNode> childList(Model model, HttpServletRequest request, Integer key) {
		logger.info("获取子节点数据");
		if (key == null) {
			return null;
		}
		return treeNodeService.findChildren(key);
	}
	
	@RequestMapping(value = { ViewPath.TREE_UPDOWN }, method = RequestMethod.POST)
	@ResponseBody
	public boolean upDown(Model model, HttpServletRequest request, Integer nodeId, String type) {
		logger.info("调整子节点顺利");
		if (type == null || nodeId == null || "".equals(type)) {
			return false;
		}
		TreeNode node = treeNodeService.find(nodeId);
		int orderNum = node.getOrderNum();
		if("up".equals(type)){
			TreeNode preNode = treeNodeService.findPreNode(node);
			if(preNode == null){
				return false;
			}
			node.setOrderNum(preNode.getOrderNum());
			preNode.setOrderNum(orderNum);			
			treeNodeService.update(preNode);
		} else if("down".equals(type)){
			TreeNode nextNode = treeNodeService.findNextNode(node);	
			if(nextNode == null){
				return false;
			}
			node.setOrderNum(nextNode.getOrderNum());
			nextNode.setOrderNum(orderNum);
			treeNodeService.update(nextNode);
		} else {
			return false;
		}		
		treeNodeService.update(node);		
		return true;
	}

}
