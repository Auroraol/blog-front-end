package com.lfj.blog.handler.security;

import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.utils.ResponseUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @Author: LFJ
 * @Date: 2024-03-10 23:13
 * 认证失败的返回
 * 目的：当用户访问了不属于自己权限的访问路径的时候，返回json格式的异常错误提示代码
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
		ResponseUtil.output(httpServletResponse, ResponseResult.noPermission());
	}
}
