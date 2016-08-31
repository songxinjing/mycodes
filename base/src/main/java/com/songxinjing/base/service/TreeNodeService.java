package com.songxinjing.base.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.songxinjing.base.constant.Constant;
import com.songxinjing.base.dao.TreeNodeDao;
import com.songxinjing.base.domain.TreeNode;
import com.songxinjing.base.domain.User;
import com.songxinjing.base.form.TreeNodeForm;
import com.songxinjing.base.service.base.BaseService;

/**
 * 配置信息服务类
 * 
 * @author songxinjing
 * 
 */
@Service
public class TreeNodeService extends BaseService<TreeNode, Integer> {

	@Autowired
	private UserService userService;

	@Autowired
	public void setSuperDao(TreeNodeDao treeNodeDao) {
		super.setDao(treeNodeDao);
	}

	/**
	 * 获取子节点数据
	 * 
	 * @param parentId
	 *            父节点ID
	 * @param isRecursion
	 *            是否递归遍历
	 * @return 子节点List
	 */
	public List<TreeNodeForm> findForm(int parentId, boolean deep) {
		if (parentId == 0) { // 默认获取根节点
			TreeNode node = find(Constant.TREE_ROOT_ID);
			List<TreeNode> list = new ArrayList<TreeNode>();
			list.add(node);
			return convert(list, deep);
		}
		List<TreeNode> list = find(parentId).getChildren();
		return convert(list, deep);
	}

	/**
	 * 遍历将List<TreeNode>转换成List<TreeNodeForm>
	 * 
	 * @param nodes
	 * @param isRecursion
	 *            是否递归遍历
	 * @return
	 */
	public List<TreeNodeForm> convert(List<TreeNode> nodes, boolean deep) {
		List<TreeNodeForm> nodeForms = new ArrayList<TreeNodeForm>();
		for (TreeNode node : nodes) {
			TreeNodeForm nodeForm = new TreeNodeForm();
			nodeForm.setKey(node.getNodeId());
			nodeForm.setTitle(node.getNodeName());
			List<TreeNode> childNodes = node.getChildren();
			if (childNodes.size() > 0) {
				nodeForm.setFolder(true);
				if (deep) {
					nodeForm.setChildren(convert(childNodes, true));
				} else {
					nodeForm.setLazy(true);
					nodeForm.setChildren(null);
				}
			}
			nodeForms.add(nodeForm);
		}
		return nodeForms;
	}

	/**
	 * 获取当前节点的上一个同级节点
	 * 
	 * @param node
	 *            当前节点
	 * @return 上一个同级节点，没有则返回null
	 */
	public TreeNode findPreNode(TreeNode node) {
		List<TreeNode> children = node.getParent().getChildren();
		int index = children.indexOf(node);
		if (index <= 0) {
			return null;
		} else {
			return children.get(index - 1);
		}
	}

	/**
	 * 获取当前节点的下一个同级节点
	 * 
	 * @param node
	 *            当前节点
	 * @return 下一个同级节点，没有则返回null
	 */
	public TreeNode findNextNode(TreeNode node) {
		List<TreeNode> children = node.getParent().getChildren();
		int index = children.indexOf(node);
		if (index < 0 || index >= children.size() - 1) {
			return null;
		} else {
			return children.get(index + 1);
		}
	}

	/**
	 * 保存用户的节点选择
	 * 
	 */
	public void saveSelected(int[] keys, String userId) {
		List<TreeNode> list = new ArrayList<TreeNode>();
		for (int i = 0; i < keys.length; i++) {
			list.add(find(keys[i]));
		}
		User user = userService.find(userId);
		user.setSelectedNodes(list);
		userService.update(user);
	}

	/**
	 * 获取用户的节点选择
	 * 
	 */
	public List<TreeNode> getSelected(String userId) {
		return userService.find(userId).getSelectedNodes();
	}

	/**
	 * 生成给定节点的子节点新顺序值
	 * 
	 * @param parentId
	 * @return
	 */
	public int genOrderNum(int parentId) {
		List<TreeNode> children = find(parentId).getChildren();
		return children.get(children.size() - 1).getOrderNum() + 1;
	}

}
