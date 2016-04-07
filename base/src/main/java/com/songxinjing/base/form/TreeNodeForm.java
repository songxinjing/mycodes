package com.songxinjing.base.form;

import java.util.List;

public class TreeNodeForm {
	
	/**
	 * 树节点唯一标识
	 */
	private int key;
	
	/**
	 * 树节点名称
	 */
	private String title;
	
	/**
	 * 是否是目录
	 */
	private boolean folder;
	
	/**
	 * 下级节点
	 */
	private List<TreeNodeForm> children;
	
	/**
	 * 是否延迟加载
	 */
	private boolean lazy;

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isFolder() {
		return folder;
	}

	public void setFolder(boolean folder) {
		this.folder = folder;
	}

	public List<TreeNodeForm> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNodeForm> children) {
		this.children = children;
	}

	public boolean isLazy() {
		return lazy;
	}

	public void setLazy(boolean lazy) {
		this.lazy = lazy;
	}

}
