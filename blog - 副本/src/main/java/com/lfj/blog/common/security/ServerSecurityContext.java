package com.lfj.blog.common.security;

import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import com.lfj.blog.common.security.details.CustomUserDetails;
import com.lfj.blog.common.security.token.AuthenticationToken;
import com.lfj.blog.exception.ApiException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 服务安全上下文, 任务上下文
 **/
public class ServerSecurityContext {
	/**
	 * 获取当前用户相信信息
	 *
	 * @return
	 */
	public static CustomUserDetails getUserDetail(boolean throwEx) {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		if (authentication == null && throwEx) {
			throw new ApiException(ResponseCodeEnum.CREDENTIALS_INVALID.getCode(), ResponseCodeEnum.CREDENTIALS_INVALID.getMessage());
		}
		if (authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal == null && throwEx) {
			throw new ApiException(ResponseCodeEnum.CREDENTIALS_INVALID.getCode(), ResponseCodeEnum.CREDENTIALS_INVALID.getMessage());
		}
		String noneUser = "anonymousUser";
		if (noneUser.equals(principal)) {
			return null;
		}
		return (CustomUserDetails) principal;
	}

	/**
	 * 获取认证信息
	 *
	 * @return org.springframework.security.core.Authentication
	 */
	public static Authentication getAuthentication() {
		SecurityContext context = SecurityContextHolder.getContext();
		return context.getAuthentication();
	}

	/**
	 * 获取AuthenticationToken
	 *
	 * @return con.lgj.blog.common.security.AuthenticationToken
	 */
	public static AuthenticationToken getAuthenticationToken(boolean throwEx) {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		if (authentication == null && throwEx) {
			throw new ApiException(ResponseCodeEnum.CREDENTIALS_INVALID.getCode(), ResponseCodeEnum.CREDENTIALS_INVALID.getMessage());
		}
		if (authentication == null) {
			return null;
		}
		Object details = authentication.getDetails();
		if (details == null && throwEx) {
			throw new ApiException(ResponseCodeEnum.CREDENTIALS_INVALID.getCode(), ResponseCodeEnum.CREDENTIALS_INVALID.getMessage());
		}
		return (AuthenticationToken) details;
	}

}
