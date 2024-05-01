package com.lfj.blog.common.swagger3.config;


import com.lfj.blog.common.swagger3.Properties.SwaggerProperties;
import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SwaggerConfig
 **/
@EnableOpenApi
@EnableWebMvc
@Configuration
public class SwaggerConfig extends WebMvcConfigurationSupport {

	@Autowired
	private SwaggerProperties swaggerProperties;

	// 解决Springfox 3.x 版本中在Spring Boot 2.6.x以上版本报错问题
	@Bean
	public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
		return new BeanPostProcessor() {

			@Override
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
					customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
				}
				return bean;
			}

			private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
				List<T> copy = mappings.stream()
						.filter(mapping -> mapping.getPatternParser() == null)
						.collect(Collectors.toList());
				mappings.clear();
				mappings.addAll(copy);
			}

			@SuppressWarnings("unchecked")
			private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
				try {
					Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
					field.setAccessible(true);
					return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new IllegalStateException(e);
				}
			}
		};
	}

	//
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)  //OAS 3.0 是较新的规范版本，在功能和表达能力上相比 Swagger 2.0 有所增强。
				// 是否启用Swagger, 在生产环境要禁用（可写到配置文件application.yml中）
				.groupName("所有路径分组")
				.enable(swaggerProperties.getEnable())
				// 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
				.apiInfo(apiInfo())
				// 设置哪些接口暴露给Swagger展示
				.select()
				// 扫描所有有注解的api，用这种方式更灵活
				//方法一、扫描类上有@Api的，推荐，不会显示basic-error-controller
				.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
				// 方法二、扫描方法上有@ApiOperation，但缺少类信息，不会显示basic-error-controller
				//.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				// 方法三、按包扫描,也可以扫描共同的父包，不会显示basic-error-controller
//                .apis(RequestHandlerSelectors.basePackage("com.alian.swagger.controller"))
//              //  .paths(PathSelectors.regex("/.*"))// 对根下所有路径进行监控
				//为任何接口生成API文档
				.paths(PathSelectors.any())
				.build()
				/* 设置安全模式，swagger可以设置访问token */
				.securitySchemes(securitySchemes())
				.securityContexts(securityContexts());
		// 统一请求前缀（可写到配置文件application.yml中）
//				.pathMapping("/dev-api");
	}

	@Bean
	public Docket createRestApi2() {
		return new Docket(DocumentationType.SWAGGER_2)  // 指定swagger3.0版本
				// 是否启用Swagger, 在生产环境要禁用（可写到配置文件application.yml中）
				.enable(swaggerProperties.getEnable())
				.groupName("lfj的分组")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.lfj.blog.controller.article"))  // 指定扫描的包  常用方式
				.build()
				.apiInfo(apiInfo2());
	}


	/**
	 * 添加摘要信息
	 */
	private ApiInfo apiInfo() {
		// 用ApiInfoBuilder进行定制
		return new ApiInfoBuilder()
				.title(swaggerProperties.getTitle())
				.description(swaggerProperties.getDescription())
				.contact(new Contact(swaggerProperties.getAuthor(),
						swaggerProperties.getUrl(), swaggerProperties.getEmail()))
				.version(swaggerProperties.getVersion())
				.build();
	}

	/**
	 * 添加摘要信息
	 *
	 * @return
	 */
	public ApiInfo apiInfo2() {
		return new ApiInfo("Java1234 Swagger"
				, "Java1234 Api Documentation"
				, "3.0"
				, "http://www.java1234.vip"
				, new Contact("小丽", "http://www.java1234.vip", "caofeng2012@126.com")
				, "Apache 2.0"
				, "http://www.apache.org/licenses/LICENSE-2.0"
				, new ArrayList());
	}

	/**
	 * 安全模式，这里指定token通过Authorization头请求头传递
	 */
	private List<SecurityScheme> securitySchemes() {
		List<SecurityScheme> apiKeyList = new ArrayList<SecurityScheme>();
		// 这里的name是
		apiKeyList.add(new ApiKey("Authorization", "Authorization", In.HEADER.toValue()));
		return apiKeyList;
	}

	/**
	 * 安全上下文
	 */
	private List<SecurityContext> securityContexts() {
		List<SecurityContext> securityContexts = new ArrayList<>();
		securityContexts.add(
				SecurityContext.builder()
						.securityReferences(defaultAuth())
						.operationSelector(o -> o.requestMappingPattern().matches("/.*"))
						.build());
		return securityContexts;
	}

	/**
	 * 默认的安全上引用
	 */
	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		List<SecurityReference> securityReferences = new ArrayList<>();
		securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
		return securityReferences;
	}

	/**
	 * 配置swagger的静态资源路径
	 *
	 * @param registry
	 */
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 解决静态资源无法访问
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/static/");
		// 解决swagger无法访问
		registry.addResourceHandler("/swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");
		// 解决swagger的js文件无法访问
		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}
