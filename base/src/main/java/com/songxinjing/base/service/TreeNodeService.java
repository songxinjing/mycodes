package com.songxinjing.base.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.songxinjing.base.dao.TreeNodeDao;
import com.songxinjing.base.domain.TreeNode;
import com.songxinjing.base.form.TreeNodeForm;

/**
 * 配置信息服务类
 * 
 * @author songxinjing
 * 
 */
@Service
public class TreeNodeService {

	@Autowired
	TreeNodeDao treeNodeDao;

	/**
	 * 获取父节点下的所有子节点（按顺序）
	 * 
	 * @param parentId
	 *            指定的父节点
	 * @return 所有子节点List
	 */
	public List<TreeNode> findChildren(int parentId) {
		return treeNodeDao.findChildren(parentId);
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
	public List<TreeNodeForm> findChildrenForm(int parentId, boolean isRecursion) {
		List<TreeNode> childNodes = findChildren(parentId);
		return convert(childNodes, isRecursion);
	}

	/**
	 * 遍历将List<TreeNode>转换成List<TreeNodeForm>
	 * 
	 * @param nodes
	 * @param isRecursion
	 *            是否递归遍历
	 * @return
	 */
	public List<TreeNodeForm> convert(List<TreeNode> nodes, boolean isRecursion) {
		List<TreeNodeForm> nodeForms = new ArrayList<TreeNodeForm>();
		for (TreeNode node : nodes) {
			TreeNodeForm nodeForm = new TreeNodeForm();
			nodeForm.setKey(node.getNodeId());
			nodeForm.setTitle(node.getNodeName());
			List<TreeNode> childNodes = treeNodeDao.findChildren(node.getNodeId());
			if (childNodes.size() > 0) {
				nodeForm.setFolder(true);
				if (isRecursion) {
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

}
