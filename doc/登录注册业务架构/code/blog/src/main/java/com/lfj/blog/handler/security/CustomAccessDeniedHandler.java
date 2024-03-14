package com.lfj.blog.handler.security;

import com.alibaba.fastjson2.JSONObject;
import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 如果用户处于登陆（authenticated）状态，会触发AccessDeniedHandler。
 * @Author: LFJ
 * @Date: 2024-03-10 23:13
 * 目的：当用户访问了不属于自己权限的访问路径的时候，返回json格式的异常错误提示代码
 */
@Component("customAccessDeniedHandler")
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest httpServletRequest,
					   HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
		ResponseUtil.output(httpServletResponse, ResponseResult.noPermission());
	}
}
