package com.lfj.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Value("${oss.type}")
	int type;

	@Value("${oss.local.path}")
	String filePath; //文件保存根路径

	@Value("${oss.local.virtualPath}")
	String virtualPath; //虚拟路径

	/**
	 * 用于本地存储,
	 * 文件上传到服务器某个目录，然后SpringBoot配置虚拟路径，映射到此目录
	 * 将匹配上/virtualPath/**虚拟路径的url映射到文件上传到服务器的绝对路径，获取静态资源
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (type == 1) {
			//将匹配上/files/**虚拟路径的url映射到文件上传到服务器的绝对路径，获取静态资源
			registry.addResourceHandler("/" + virtualPath + "/**")
					.addResourceLocations("file:" + filePath);
			WebMvcConfigurer.super.addResourceHandlers(registry);
		}
	}
}
