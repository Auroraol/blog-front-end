package com.huan.study.security.configuration;

import com.huan.study.security.token.TokenAuthenticateFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author huan.fu 2020-06-07 - 13:34
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final TokenAuthenticateFilter tokenAuthenticateFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                // 设置 session 为无状态，因为基于 token 不需要 session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .sessionFixation().none()
                .and()
            .authorizeRequests()
                .antMatchers("/getToken/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .csrf().disable()
            .addFilterBefore(tokenAuthenticateFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
