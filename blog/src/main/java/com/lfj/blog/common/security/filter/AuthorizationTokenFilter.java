package com.lfj.blog.common.security.filter;

import com.alibaba.fastjson2.JSON;
import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.common.security.AuthenticationToken;
import com.lfj.blog.common.security.RedisTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthenticationToken 校验过滤器
 * 认证结果过滤器
 **/
@Component
public class AuthorizationTokenFilter extends OncePerRequestFilter {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String TOKEN_TYPE = "Bearer";
	@Autowired
	private RedisTokenStore tokenStore;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		final String authorization = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
		if (authorization != null && authorization.startsWith(TOKEN_TYPE)) {
			String accessToken = authorization.substring(7);
			if (!accessToken.isEmpty()) {
				AuthenticationToken cacheAuthenticationToken = tokenStore.readByAccessToken(accessToken);
				if (cacheAuthenticationToken == null) {
					httpServletResponse.setCharacterEncoding("UTF-8");
					httpServletResponse.setContentType("application/json; charset=utf-8");
					// ApiResponseResult.invalidRequest() 返回结果: 无效请求
					httpServletResponse.getWriter().print(JSON.toJSON(ApiResponseResult.invalidRequest()));
					return;
				}
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(cacheAuthenticationToken.getPrincipal(), null, cacheAuthenticationToken.getPrincipal().getAuthorities());
				authentication.setDetails(cacheAuthenticationToken);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

}
