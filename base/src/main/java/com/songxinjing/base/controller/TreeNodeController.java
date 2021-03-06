package com.songxinjing.base.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.songxinjing.base.constant.Constant;
import com.songxinjing.base.controller.base.BaseController;
import com.songxinjing.base.domain.TreeNode;
import com.songxinjing.base.domain.User;
import com.songxinjing.base.form.TreeNodeForm;
import com.songxinjing.base.service.TreeNodeService;

/**
 * 树型控制类
 * 
 * @author songxinjing
 * 
 */
@Controller
public class TreeNodeController extends BaseController {

	@Autowired
	TreeNodeService treeNodeService;

	@RequestMapping(value = "tree/index", method = RequestMethod.GET)
	public String index(Model model, HttpServletRequest request) {
		logger.info("进入Tree页面");
		model.addAttribute("menu", "tree");
		return "tree/index";
	}

	@RequestMapping(value = "tree/data", method = RequestMethod.GET)
	@ResponseBody
	public List<TreeNodeForm> data(Model model, HttpServletRequest request, Integer key, Boolean deep) {
		logger.info("获取Tree的数据");
		if (key == null) {
			key = 0;
		}
		if (deep == null) {
			deep = false;
		}
		return treeNodeService.findForm(key, deep);
	}

	@RequestMapping(value = "tree/edit", method = RequestMethod.GET)
	public String edit(Model model, HttpServletRequest request) {
		logger.info("进入Tree编辑页面");
		model.addAttribute("menu", "tree");
		return "tree/edit";
	}

	@RequestMapping(value = "tree/saveSelected", method = RequestMethod.POST)
	@ResponseBody
	public boolean saveSelected(Model model, HttpServletRequest request, int[] keys) {
		logger.info("保存选中节点");
		User loginUser = (User) request.getSession().getAttribute(Constant.SESSION_LOGIN_USER);
		treeNodeService.saveSelected(keys, loginUser.getUserId());
		return true;
	}

	@RequestMapping(value = "tree/getSelected", method = RequestMethod.POST)
	@ResponseBody
	public List<TreeNode> getSelected(Model model, HttpServletRequest request) {
		logger.info("获取选中节点");
		User loginUser = (User) request.getSession().getAttribute(Constant.SESSION_LOGIN_USER);
		if (loginUser != null) {
			return treeNodeService.getSelected(loginUser.getUserId());
		}
		return null;
	}

	@RequestMapping(value = "tree/save", method = RequestMethod.POST)
	@ResponseBody
	public boolean save(Model model, HttpServletRequest request, Integer key, String type, String nodeName) {
		logger.info("保存Tree的数据");
		if (type == null || key == null || "".equals(nodeName)) {
			return false;
		}
		if ("A".equals(type)) { // 新增
			TreeNode treeNode = new TreeNode();
			treeNode.setNodeName(nodeName);
			treeNode.setParent(treeNodeService.find(key));
			treeNode.setOrderNum(treeNodeService.genOrderNum(key));
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

	@RequestMapping(value = "tree/del", method = RequestMethod.POST)
	@ResponseBody
	public boolean del(Model model, HttpServletRequest request, Integer key) {
		logger.info("删除节点");
		if (key == null) {
			return false;
		}
		treeNodeService.delete(key);
		return true;
	}

	@RequestMapping(value = "tree/childlist", method = RequestMethod.POST)
	@ResponseBody
	public List<TreeNode> childList(Model model, HttpServletRequest request, Integer key) {
		logger.info("获取子节点数据");
		if (key == null) {
			return null;
		}
		return treeNodeService.find(key).getChildren();
	}

	@RequestMapping(value = "tree/updown", method = RequestMethod.POST)
	@ResponseBody
	public boolean upDown(Model model, HttpServletRequest request, Integer nodeId, String type) {
		logger.info("调整子节点顺利");
		if (type == null || nodeId == null || "".equals(type)) {
			return false;
		}
		TreeNode node = treeNodeService.find(nodeId);
		int orderNum = node.getOrderNum();
		if ("up".equals(type)) {
			TreeNode preNode = treeNodeService.findPreNode(node);
			if (preNode == null) {
				return false;
			}
			node.setOrderNum(preNode.getOrderNum());
			preNode.setOrderNum(orderNum);
			treeNodeService.update(preNode);
		} else if ("down".equals(type)) {
			TreeNode nextNode = treeNodeService.findNextNode(node);
			if (nextNode == null) {
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
