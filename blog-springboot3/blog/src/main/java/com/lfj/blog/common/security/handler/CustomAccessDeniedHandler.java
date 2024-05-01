package com.lfj.blog.common.security.handler;

import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.utils.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 授权过程中出现的异常
 *
 * @Author: LFJ
 * @Date: 2024-03-25 17:24
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		//授权失败, 不允许访问
		ResponseUtil.output(response, ApiResponseResult.access_denied());
	}
}
