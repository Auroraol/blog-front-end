package com.lfj.blog.common.security;

import lombok.Data;

/**
 * @Author: LFJ
 * @Date: 2024-03-09 15:55
 *  Token 实体类
 */
@Data
public class Token {
	/**
	 * 访问token
	 */
	private String accessToken;

	/**
	 * 刷新token
	 */
	private String refreshToken;

}
