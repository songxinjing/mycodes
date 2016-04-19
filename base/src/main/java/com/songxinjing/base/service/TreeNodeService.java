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
	 * 根据节点ID获取节点对象
	 * 
	 * @param nodeId
	 *            节点ID
	 * 
	 * @return TreeNode节点对象
	 */
	public TreeNode find(int nodeId) {
		return treeNodeDao.findByPK(nodeId);
	}

	/**
	 * 根据节点ID删除节点对象
	 * 
	 * @param nodeId
	 *            节点ID
	 */
	public void delete(int nodeId) {
		treeNodeDao.delete(nodeId);
	}

	/**
	 * 根据节点对象更新数据库
	 * 
	 * @param treeNode
	 *            节点对象
	 */
	public void update(TreeNode treeNode) {
		treeNodeDao.update(treeNode);
	}

	/**
	 * 保存新节点
	 * 
	 * @param treeNode
	 * 
	 * @return nodeId
	 */
	public int save(TreeNode treeNode) {
		return (Integer) treeNodeDao.save(treeNode);
	}

	/**
	 * 生成同级排序序号
	 * 
	 * @param parentId
	 *            指定的父节点
	 * @return 同级排序序号
	 */
	public int genOrderNum(int parentId) {
		return treeNodeDao.findMaxOrderNum(parentId) + 1;
	}

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
	public List<TreeNodeForm> findChildrenForm(int parentId, boolean deep) {
		List<TreeNode> childNodes = findChildren(parentId);
		return convert(childNodes, deep);
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
			List<TreeNode> childNodes = treeNodeDao.findChildren(node.getNodeId());
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
		return treeNodeDao.findPreNode(node);
	}

	/**
	 * 获取当前节点的下一个同级节点
	 * 
	 * @param node
	 *            当前节点
	 * @return 下一个同级节点，没有则返回null
	 */
	public TreeNode findNextNode(TreeNode node) {
		return treeNodeDao.findNextNode(node);
	}

}
