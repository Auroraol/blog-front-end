package com.lfj.blog.common.security;

/**
 * @Author: LFJ
 * @Date: 2024-03-09 17:33
 * token角色类型
 */
public enum UserEnums {
	/**
	 * 角色
	 */
	MANAGER("管理员"),
	USER("用户");
	private final String role;

	UserEnums(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}
}
