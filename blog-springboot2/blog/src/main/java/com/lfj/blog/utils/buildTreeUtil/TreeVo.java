package com.lfj.blog.utils.buildTreeUtil;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: LFJ
 * @Date: 2024-04-06 16:19
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreeVo<T> {
	/**
	 * 节点ID
	 */
	private String id;
	/**
	 * 显示节点文本
	 */
	private String text;
	/**
	 * 节点状态，open closed
	 */
	private Map<String, Object> state;
	/**
	 * 节点是否被选中 true false
	 */
	private boolean checked = false;
	/**
	 * 节点属性
	 */
	private Map<String, Object> attributes;

	/**
	 * 节点的子节点 List<TreeVo<T>>
	 */
	private List<TreeVo<T>> children = new ArrayList<TreeVo<T>>();

	/**
	 * 父ID
	 */
	private String parentId;
	/**
	 * 是否有父节点
	 */
	private boolean hasParent = false;
	/**
	 * 是否有子节点
	 */
	private boolean hasChildren = false;

	public TreeVo(String id, String text, Map<String, Object> state, boolean checked, Map<String, Object> attributes,
				  List<TreeVo<T>> children, boolean isParent, boolean isChildren, String parentID) {
		super();
		this.id = id;
		this.text = text;
		this.state = state;
		this.checked = checked;
		this.attributes = attributes;
		this.children = children;
		this.hasParent = isParent;
		this.hasChildren = isChildren;
		this.parentId = parentID;
	}

	public TreeVo() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Map<String, Object> getState() {
		return state;
	}

	public void setState(Map<String, Object> state) {
		this.state = state;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public List<TreeVo<T>> getChildren() {
		return children;
	}

	public void setChildren(List<TreeVo<T>> children) {
		this.children = children;
	}

	public void setChildren(boolean isChildren) {
		this.hasChildren = isChildren;
	}

	public boolean isHasParent() {
		return hasParent;
	}

	public void setHasParent(boolean isParent) {
		this.hasParent = isParent;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return "TreeVo [id=" + id + ", text=" + text + ", state=" + state + ", checked=" + checked + ", attributes="
				+ attributes + ", children=" + children + ", parentId=" + parentId + ", hasParent=" + hasParent
				+ ", hasChildren=" + hasChildren + "]";
	}

}