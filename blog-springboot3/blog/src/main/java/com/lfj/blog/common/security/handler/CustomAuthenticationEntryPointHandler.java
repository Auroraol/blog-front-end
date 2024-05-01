package com.lfj.blog.common.security.handler;

import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.utils.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 认证过程中出现的异常
 *
 * @Author: LFJ
 * @Date: 2024-03-25 17:24
 */
@Component
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		// 认证失败，无权限访问，请重新登录
		ResponseUtil.output(response, ApiResponseResult.noPermission());
	}

}


/**
 * 参考:
 * //    认证失败的返回权限不足
 * private AuthenticationEntryPoint authenticationEntryPoint() {
 * return (HttpServletRequest var1, HttpServletResponse var2, AuthenticationException var3) -> {
 * if (var3 instanceof InsufficientAuthenticationException) {
 * var2.setCharacterEncoding("UTF-8");
 * var2.setContentType("application/json; charset=utf-8");
 * // 无权访问
 * var2.getWriter().print(JSON.toJSON(ApiResponseResult.noPermission()));
 * }
 * };
 * }
 */