package com.lfj.blog.handler.security;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lfj.blog.common.cache.CachePrefix;
import com.lfj.blog.common.security.AuthUser;
import com.lfj.blog.common.security.UserEnums;
import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.utils.token.SecretKeyUtil;
import com.lfj.blog.utils.token.SecurityKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.lfj.blog.utils.ResponseUtil;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 认证结果过滤器
 * @Author: LFJ
 * @Date: 2024-03-10 22:59
 *
 */
@Slf4j
public class UserAuthenticationFilter extends BasicAuthenticationFilter {
	private StringRedisTemplate redisTemplate;

	/**
	 * 自定义构造器
	 *
	 * @param authenticationManager
	 */
	public UserAuthenticationFilter(AuthenticationManager authenticationManager
			, StringRedisTemplate redisTemplate) {
		super(authenticationManager);
		this.redisTemplate = redisTemplate;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException, IOException {

		//从header中获取jwt
		String jwt = request.getHeader(SecurityKey.ACCESS_TOKEN);
		try {
			//如果请求头没有携带token 则return
			if (StringUtils.isBlank(jwt)) {
				chain.doFilter(request, response);
				return;
			}

			//获取用户信息，存入context上下文
			UsernamePasswordAuthenticationToken authentication = getAuthentication(jwt, response,request,  chain);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (Exception e) {
			log.error("BuyerAuthenticationFilter-> member authentication exception:", e);
		}
		// 继续过滤链
		chain.doFilter(request, response);
	}

	/**
	 * 解析用户
	 * 读取Token信息，创建UsernamePasswordAuthenticationToken对象
	 * @param jwt
	 * @param response
	 * @return
	 */
	private UsernamePasswordAuthenticationToken getAuthentication(String jwt,
																  HttpServletResponse response,
																  HttpServletRequest request,
																  FilterChain chain)
			throws ServletException, IOException {

		try {
			Claims claims = Jwts.parser()
					.verifyWith(SecretKeyUtil.generalKey()) // 传递密钥
					.build()
					.parseSignedClaims(jwt)//传递jwt令牌参数
					.getPayload();  // 获取- Payload(有效载荷）
			//获取存储在claims中的用户信息
			String json = claims.get(SecurityKey.USER_CONTEXT).toString();
			AuthUser authUser = JSON.parseObject(json, AuthUser.class);


			//校验redis中是否有权限
			Boolean hasKey = redisTemplate.hasKey(CachePrefix.ACCESS_TOKEN.name() + UserEnums.USER.name() + jwt);
			if (hasKey != null && hasKey) {
				//构造返回信息
				List<GrantedAuthority> auths = new ArrayList<>();
				auths.add(new SimpleGrantedAuthority("ROLE_" + authUser.getRole().name()));
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authUser.getUsername(), null, auths);
				authentication.setDetails(authUser);
				return authentication;
			}

			return null;
		} catch (ExpiredJwtException e) {
			// 如果捕获到 ExpiredJwtException 异常，则 JWT 已经过期
//			ResponseUtil.output(response, ResponseResult.tokenError());  // 400100
			return null;

		} catch (Exception e) {
			log.error("user analysis exception:", e);
		}
		return null;
	}

}
