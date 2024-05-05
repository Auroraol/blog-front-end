package com.lfj.blog.common.security.filter;

import com.alibaba.fastjson2.JSON;
import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.common.security.token.AuthenticationToken;
import com.lfj.blog.common.security.token.RedisTokenStore;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * AuthenticationToken 校验
 * 认证结果过滤器
 **/
@Component
public class AuthorizationTokenFilter extends OncePerRequestFilter {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String TOKEN_TYPE = "Bearer";
	@Autowired
	private RedisTokenStore tokenStore;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest,
									HttpServletResponse httpServletResponse,
									FilterChain filterChain) throws ServletException, IOException {
		final String authorization = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
		if (authorization != null && authorization.startsWith(TOKEN_TYPE)) {
			String accessToken = authorization.substring(7);
			if (!accessToken.isEmpty()) {
				AuthenticationToken cacheAuthenticationToken = tokenStore.readByAccessToken(accessToken);
//				System.out.println(cacheAuthenticationToken);
				if (cacheAuthenticationToken == null) {
					httpServletResponse.setCharacterEncoding("UTF-8");
					httpServletResponse.setContentType("application/json; charset=utf-8");
					// ApiResponseResult.invalidRequest() 返回结果: 无效请求
					httpServletResponse.getWriter().print(JSON.toJSON(ApiResponseResult.invalidRequest()));
					return;
				}
				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(cacheAuthenticationToken.getPrincipal(),
								null, cacheAuthenticationToken.getPrincipal().getAuthorities());
				// 放入任务上下文
				authentication.setDetails(cacheAuthenticationToken);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		System.out.println();
		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}
}
