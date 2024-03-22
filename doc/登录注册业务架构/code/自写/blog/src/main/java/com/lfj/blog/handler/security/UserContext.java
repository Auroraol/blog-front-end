package com.lfj.blog.handler.security;

import com.lfj.blog.common.security.AuthUser;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

/**
 * 用户上下文
 * @Author: LFJ
 * @Date: 2024-03-13 17:56
 */
public class UserContext {
	private static AuthenticationHandler authenticationHandler;

	public static void setHolder(AuthenticationHandler authenticationHandler) {
		UserContext.authenticationHandler = authenticationHandler;
	}


	public static AuthUser getCurrentUser() {
		return authenticationHandler.getAuthUser();
	}

}
