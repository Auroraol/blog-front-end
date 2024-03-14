package com.lfj.blog.handler.security;

import com.alibaba.fastjson2.JSONObject;
import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 如果用户处于未登录（anonymous）状态，会先触发AuthenticationEntryPoint，如果没有配置，则会重定向至登录页；
 * @Author: LFJ
 * @Date: 2024-03-13 17:21
 */
@Component("customAuthenticationEntryPoint")
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException, IOException {
		ResponseUtil.output(response, ResponseResult.noPermission());
	}
}