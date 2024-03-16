//package com.lfj.blog.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
///**
// * @Author: LFJ
// * @Date: 2024-03-11 23:16
// */
//@Configuration
//public class CorsConfig {
//	@Bean
//	public CorsFilter corsFilter() {
//		//1.添加CORS配置信息
//		CorsConfiguration config = new CorsConfiguration();
//		//1) 允许的域,不要写*，否则cookie就无法使用了   http://localhost:8888
//		config.addAllowedOrigin("http://localhost:8888"); //这里填写请求的前端服务器
//		//2) 是否发送Cookie信息
//		config.setAllowCredentials(true);
//		//3) 允许的请求方式
//		config.addAllowedMethod("OPTIONS");
//		config.addAllowedMethod("HEAD");
//		config.addAllowedMethod("GET");
//		config.addAllowedMethod("PUT");
//		config.addAllowedMethod("POST");
//		config.addAllowedMethod("DELETE");
//		config.addAllowedMethod("PATCH");
//		// 4）允许的头信息
//		config.addAllowedHeader("*");
//
//		//2.添加映射路径，我们拦截一切请求
//		UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
//		configSource.registerCorsConfiguration("/**", config);
//
//		//3.返回新的CorsFilter.
//		return new CorsFilter(configSource);
//	}
//}

// TODO
//前端,后端一个实现跨域就行了