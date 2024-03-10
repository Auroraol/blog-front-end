package com.lfj.blog.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @Author: LFJ
 * @Date: 2024-03-10 22:55
 */
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserSecurityConfig {
	@Autowired
	private StringRedisTemplate redisTemplate;

	/**
	 * 忽略验权配置
	 */
	@Autowired
	private IgnoredUrlsProperties ignoredUrlsProperties;

	/**
	 * spring security -》 权限不足处理
	 */
	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
				.authorizeRequests();
		//配置的url 不需要授权
		for (String url : ignoredUrlsProperties.getUrls()) {
			registry.antMatchers(url).permitAll();
		}
		registry
				.and()
				//禁止网页iframe
				.headers().frameOptions().disable()
				.and()
				.logout()
				.permitAll()
				.and()
				.authorizeRequests()
				//任何请求
				.anyRequest()
				//需要身份认证
				.authenticated()
				.and()
				//允许跨域
				.cors().and()
				//关闭跨站请求防护
				.csrf().disable()
				//前后端分离采用JWT 不需要session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				//自定义权限拒绝处理类
				.exceptionHandling().accessDeniedHandler(accessDeniedHandler)
				.and()
				//添加JWT认证过滤器
				.addFilter(new BuyerAuthenticationFilter(authenticationManager(), redisTemplate));
	}

}
