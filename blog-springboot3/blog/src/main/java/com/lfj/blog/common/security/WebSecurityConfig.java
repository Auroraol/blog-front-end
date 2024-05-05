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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * 安全配置
 **/
@Log4j2
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 开启授权
public class WebSecurityConfig {

	@Autowired
	private CustomAuthenticationEntryPointHandler customAuthenticationEntryPointHandler;
	@Autowired
	private CustomAccessDeniedHandler customAccessDeniedHandler;
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

	/**
	 * 这里是对认证管理器的添加配置
	 * 新版AuthenticationManager认证管理器默认全局）
	 * 调用UserDetailsServiceImp中loadUserByUsername获得UserDetail信息，
	 * 在AbstractUserDetailsAuthenticationProvider里执行用户状态检查
	 *
	 * @return
	 */
	@Bean
	AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService); // 确认用户数据的来源
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());     //加密
		ProviderManager pm = new ProviderManager(daoAuthenticationProvider);
		return pm;
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
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		List<String> ignorePropertiesList = ignoreProperties.getList();
		int size = ignorePropertiesList.size();

		http.httpBasic(AbstractHttpConfigurer::disable)          // 禁用basic明文验证
				.cors(Customizer.withDefaults())  //跨域
				.csrf(AbstractHttpConfigurer::disable) //前后端分离架构不需要csrf保护
				.formLogin(AbstractHttpConfigurer::disable)   // 禁用默认登录页
				.authorizeHttpRequests(request ->
						request.requestMatchers(ignorePropertiesList.toArray(new String[size])).permitAll()  //配置的url 不需要授权
								.anyRequest().authenticated()  // 剩下的任意请求都需要认证
				)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 前后端分离是无状态的，不需要session了，直接禁用。
				.logout((logout) -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))); // 退出

		http.authenticationProvider(provider())//自定义手机验证码认证提供者
				.authenticationProvider(provider2());//自定义主键查询用户认证提供者(用于第三方登录)

		// 添加认证过滤器(自定义)
		// 第一个参数是要添加的过滤器对象，第二个参数是指定在哪一个现有过滤器之前添加这个自定义过滤器。
		// 实现在进行用户名密码认证之前对请求进行自定义的认证逻辑处理。
		http.addFilterBefore(authorizationTokenFilter, UsernamePasswordAuthenticationFilter.class);

		// 配置异常处理器(自定义未认证和未授权异常)，如果不设置，默认使用Http403ForbiddenEntryPoint
		http.exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(customAuthenticationEntryPointHandler).
				accessDeniedHandler(customAccessDeniedHandler));

		return http.build();
	}


	//配置WebSecurity
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers(
//				antMatcher("/css/**"),
//				antMatcher("/fonts/**"),
//				antMatcher("/img/**"),
//				antMatcher("/js/**"),
//				antMatcher("/widget/js/**"),
//				antMatcher("/favicon.ico"),
//				antMatcher("/*.html"),
				antMatcher("/favicon.ico"),
				antMatcher("/**/*.html"),
				antMatcher("/**/*.css"),
				antMatcher("/**/*.js"))
				;
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


