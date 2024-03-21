package com.huan.study.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * 给 app 端用的 Security 配置
 *
 * @author huan.fu 2021/7/13 - 下午9:06
 */
@Configuration
public class AppSecurityConfig {

    /**
     * 处理 给 app(前后端分离) 端使用的过滤链
     * 以 json 的数据格式返回给前端
     */
    @Bean
    @Order(1)
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
        // 只处理 /api 开头的请求
        return http.antMatcher("/api/**")
                .authorizeRequests()
                // 所有以 /api 开头的请求都需要 ADMIN 的权限
                    .antMatchers("/api/**")
                    .hasRole("ADMIN")
                    .and()
                // 捕获到异常，直接给前端返回 json 串
                .exceptionHandling()
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                        response.setContentType(MediaType.APPLICATION_JSON.toString());
                        response.getWriter().write("{\"message:\":\"您无权访问01\"}");
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                        response.setContentType(MediaType.APPLICATION_JSON.toString());
                        response.getWriter().write("{\"message:\":\"您无权访问02\"}");
                    })
                    .and()
                // 用户认证
                .addFilterBefore((request, response, chain) -> {
                    // 此处可以模拟从 token 中解析出用户名、权限等
                    String token = ((HttpServletRequest) request).getHeader("token");
                    if (!StringUtils.hasText(token)) {
                        chain.doFilter(request, response);
                        return;
                    }
                    Authentication authentication = new TestingAuthenticationToken(token, null,
                            AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    chain.doFilter(request, response);
                }, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
