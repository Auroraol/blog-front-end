package com.lfj.blog.common.security;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: LFJ
 * @Date: 2024-03-09 17:32
 *  存储到Token中的信息
 */
@Data
@AllArgsConstructor
public class AuthUser implements Serializable {

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * id
	 */
	private String id;

	/**
	 * 长期有效（用于手机app登录场景或者信任场景等）
	 */
	private Boolean longTerm = false;

	/**
	 * @see UserEnums
	 * 角色
	 */
	private UserEnums role;


	/**
	 * 是否是超级管理员
	 */
	private Boolean isSuper = false;

	public AuthUser(String username, String id, String nickName, UserEnums role) {
		this.username = username;
		this.id = id;
		this.role = role;
		this.nickName = nickName;
	}

	public AuthUser(String username, String id, UserEnums manager, String nickName, Boolean isSuper) {
		this.username = username;
		this.id = id;
		this.role = manager;
		this.isSuper = isSuper;
		this.nickName = nickName;
	}
}
