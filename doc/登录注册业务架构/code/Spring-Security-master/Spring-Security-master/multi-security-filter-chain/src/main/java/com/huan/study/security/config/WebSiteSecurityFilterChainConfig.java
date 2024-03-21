package com.huan.study.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 给 网站 应用的安全配置
 *
 * @author huan.fu 2021/7/14 - 上午9:09
 */
@Configuration
public class WebSiteSecurityFilterChainConfig {
    /**
     * 处理 给 webSite（非前后端分离） 端使用的过滤链
     * 以 页面 的格式返回给前端
     */
    @Bean
    @Order(2)
    public SecurityFilterChain webSiteSecurityFilterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        // 创建用户
        authenticationManagerBuilder.inMemoryAuthentication()
                .withUser("admin")
                    .password(new BCryptPasswordEncoder().encode("admin"))
                    .authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"))
                    .and()
                .withUser("dev")
                    .password(new BCryptPasswordEncoder().encode("dev"))
                    .authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_DEV"))
                    .and()
                .passwordEncoder(new BCryptPasswordEncoder());

        // 只处理 所有 开头的请求
        return http.antMatcher("/**")
                .authorizeRequests()
                // 所有请求都必须要认证才可以访问
                    .anyRequest()
                    .hasRole("ADMIN")
                    .and()
                // 禁用csrf
                .csrf()
                    .disable()
                // 启用表单登录
                .formLogin()
                    .permitAll()
                    .and()
                // 捕获成功认证后无权限访问异常，直接跳转到 百度
                .exceptionHandling()
                    .accessDeniedHandler((request, response, exception) -> response.sendRedirect("http://www.baidu.com"))
                    .and()
                .build();
    }

    /**
     * 忽略静态资源
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer( ){
        return web -> web.ignoring()
                .antMatchers("/**/js/**")
                .antMatchers("/**/css/**");

    }
}
