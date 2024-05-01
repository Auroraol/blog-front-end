package com.lfj.blog.utils.buildTreeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BuildTree 是一个开发中常用的工具类，用于构建树形结构。它封装了一系列方法，使得构建树形结构变得简单和高效。使用 BuildTree 工具类的优势包括：
 * • 简化开发流程：BuildTree 工具类提供了一种简单直观的方式来构建树形结构，不需要手动编写复杂的逻辑。
 * • 提高代码可读性和可维护性：通过使用 BuildTree 工具类，代码的意图更加清晰明了，降低了出错的几率，并且简化了后续的维护工作。
 *
 * @Author: LFJ
 * @Date: 2024-04-06 16:17
 */
public class BuildTreeUtil {

	/**
	 * 默认-1为顶级节点
	 *
	 * @param nodes
	 * @param <T>
	 * @return
	 */
	public static <T> TreeVo<T> build(List<TreeVo<T>> nodes) {

		// 如果节点列表为空，则返回 null
		if (nodes == null) {
			return null;
		}

		// 创建一个空的顶级节点列表
		List<TreeVo<T>> topNodes = new ArrayList<TreeVo<T>>();

		for (TreeVo<T> children : nodes) {
			String pid = children.getParentId(); //获取当前对象的父级id

			// 如果父节点ID为null或者"-1"，则将当前节点视为顶级节点，添加到顶级节点列表中
			if (pid == null || "-1".equals(pid)) {
				topNodes.add(children);

				continue;
			}

			// 遍历节点列表，将当前节点添加到对应的父节点的子节点列表中
			for (TreeVo<T> parent : nodes) {
				String id = parent.getId();
				if (id != null && id.equals(pid)) {
					parent.getChildren().add(children);
					children.setHasParent(true);
					parent.setChildren(true);
					continue;
				}
			}
		}

		TreeVo<T> root = new TreeVo<T>();

		// 根据顶级节点列表的大小决定返回结果
		if (topNodes.size() == 1) {
			// 如果顶级节点列表只有一个节点，将其作为根节点返回
			root = topNodes.get(0);
		} else {
			// 如果顶级节点列表为空或包含多个节点，创建一个虚拟的根节点，将顶级节点列表作为其子节点，然后返回根节点
			root.setId("000");
			root.setParentId("-1");
			root.setHasParent(false);
			root.setChildren(true);
			root.setChecked(true);
			root.setChildren(topNodes);
			root.setText("顶级节点");
			Map<String, Object> state = new HashMap<>(16);
			state.put("opened", true);
			root.setState(state);
		}

		return root;
	}

	/**
	 * 指定idParam为顶级节点
	 *
	 * @param nodes   带有父子关系的数据
	 * @param idParam 顶级节点id，当其id=idParam时为顶级父节点
	 * @param <T>
	 * @return
	 */
	public static <T> List<TreeVo<T>> buildList(List<TreeVo<T>> nodes, String idParam) {
		// 如果节点列表为空，则返回 null
		if (nodes == null) {
			return null;
		}

		// 创建一个空的顶级节点列表
		List<TreeVo<T>> topNodes = new ArrayList<TreeVo<T>>();

		for (TreeVo<T> children : nodes) {

			String pid = children.getParentId();

			// 如果父节点ID为null或与idParam相等，则将当前节点视为顶级节点，添加到顶级节点列表中
			if (pid == null || idParam.equals(pid)) {
				topNodes.add(children);

				continue;
			}

			// 遍历节点列表，将当前节点添加到对应的父节点的子节点列表中
			for (TreeVo<T> parent : nodes) {
				String id = parent.getId();
				if (id != null && id.equals(pid)) {
					parent.getChildren().add(children);
					children.setHasParent(true);
					parent.setChildren(true);

					continue;
				}
			}

		}
		return topNodes;
	}

	/**
	 * 转换函数, 自己实现赋值
	 */
	public static <T> List<TreeVo<T>> convertToTreeNodes(List<T> list) {
		List<TreeVo<T>> treeNodes = new ArrayList<>();
		for (T l : list) {
			TreeVo<T> treeNode = new TreeVo<>();
			// 修改
//			treeNode.setId(String.valueOf(l.getId()));
//			treeNode.setParentId(String.valueOf(l.getParentId()));
//			treeNode.setText(l.getName());
			treeNodes.add(treeNode);
		}
		return treeNodes;
	}


}