package com.lfj.blog.common.security;

import com.lfj.blog.common.security.filter.AuthorizationTokenFilter;
import com.lfj.blog.common.security.handler.CustomAccessDeniedHandler;
import com.lfj.blog.common.security.handler.CustomAuthenticationEntryPointHandler;
import com.lfj.blog.common.sms.service.SmsCodeService;
import com.lfj.blog.service.IUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

/**
 * 安全配置
 * <p>
 * 2019-10-24 16:17
 **/
@Log4j2
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启授权
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	CustomAuthenticationEntryPointHandler customAuthenticationEntryPointHandler;
	@Autowired
	CustomAccessDeniedHandler customAccessDeniedHandler;
	@Autowired
	private SecurityIgnoreProperties ignoreProperties;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthorizationTokenFilter authorizationTokenFilter;
	@Autowired
	private SmsCodeService smsCodeService;
	@Autowired
	private IUserService userService;


//	@Bean
//	public UserDetailsService userDetailsService() {
//		return super.userDetailsService();
//	}
//

	/**
	 * 配置认证管理器
	 *
	 * @return
	 * @throws Exception
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * 这里是对认证管理器的添加配置
	 *
	 * @param auth
	 * @throws Exception
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(provider())//自定义手机验证码认证提供者
				.authenticationProvider(provider2())//自定义主键查询用户认证提供者(用于第三方登录)
				.userDetailsService(userDetailsService) // 确认用户数据的来源
				.passwordEncoder(passwordEncoder());
	}

	/**
	 * 配置密码加密对象（解密时会用到PasswordEncoder的matches判断是否正确）
	 * 用户的password用到，所以存的时候存该bean encode过的密码
	 *
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	//	配置HttpSecurity
	protected void configure(HttpSecurity http) throws Exception {
		List<String> ignorePropertiesList = ignoreProperties.getList();
		int size = ignorePropertiesList.size();

		http.cors()   //允许跨域
				.and()
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers(ignorePropertiesList.toArray(new String[size])).permitAll()  //配置的url 不需要授权
				//任何请求
				.anyRequest().authenticated()
				.and()
				.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"));

		//添加认证过滤器(自定义)
		// 第一个参数是要添加的过滤器对象，第二个参数是指定在哪一个现有过滤器之前添加这个自定义过滤器。
		//实现在进行用户名密码认证之前对请求进行自定义的认证逻辑处理。
		http.addFilterBefore(authorizationTokenFilter, UsernamePasswordAuthenticationFilter.class);

		//配置异常处理器(自定义未认证和未授权异常)
		http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPointHandler).
				accessDeniedHandler(customAccessDeniedHandler);
	}


	//	配置WebSecurity
	@Override
	public void configure(WebSecurity web) {
		web
				.ignoring()
				.antMatchers(
						HttpMethod.GET,
						"/*.html",
						"/favicon.ico",
						"/**/*.html",
						"/**/*.css",
						"/**/*.js"
				);
	}

	/**
	 * 自定义手机验证码认证提供者
	 *
	 * @return
	 */
	@Bean
	public MobileCodeAuthenticationProvider provider() {
		MobileCodeAuthenticationProvider provider = new MobileCodeAuthenticationProvider();
		provider.setSmsCodeService(smsCodeService);
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}

	/**
	 * 自定义主键查询用户认证提供者
	 *
	 * @return
	 */
	@Bean
	public PrimaryKeyAuthenticationProvider provider2() {
		PrimaryKeyAuthenticationProvider provider = new PrimaryKeyAuthenticationProvider();
		provider.setUserService(userService);
		return provider;
	}
}


