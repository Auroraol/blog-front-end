package com.lfj.blog.common.swagger3;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Configuration
public class SpringDocConfig {

//    @Bean
//    public GroupedOpenApi productApi() {
//        return GroupedOpenApi.builder()
//                .group("product-service")
//                .pathsToMatch("/product/**")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi orderApi() {
//        return GroupedOpenApi.builder()
//                .group("order-service")
//                .pathsToMatch("/order/**")
//                .build();
//    }

	@Bean
	public OpenAPI openAPI() {
		// 联系人信息(contact)，构建API的联系人信息，用于描述API开发者的联系信息，包括名称、URL、邮箱等
		Contact contact = new Contact()
				.name("robin")  // 作者名称
				.email("code9342@gmail.com") // 作者邮箱
				.url("https://www.cnblogs.com/vic-tory/") // 介绍作者的URL地址
				.extensions(new HashMap<String, Object>());// 使用Map配置信息（如key为"name","email","url"）

		License license = new License()
				.name("Apache 2.0")                         // 授权名称
				.url("https://www.apache.org/licenses/LICENSE-2.0.html")    // 授权信息
				.identifier("Apache-2.0")                   // 标识授权许可
				.extensions(new HashMap<String, Object>());// 使用Map配置信息（如key为"name","url","identifier"）

		//创建Api帮助文档的描述信息、联系人信息(contact)、授权许可信息(license)
		Info info = new Info()
				.title("Api接口文档标题")      // Api接口文档标题（必填）
				.description("项目描述")     // Api接口文档描述
				.version("1.0.0")                                  // Api接口版本
				.termsOfService("https://www.cnblogs.com/vic-tory/")    // Api接口的服务条款地址
				.license(license)  //   授权名称
				.contact(contact); // 设置联系人信息

		List<Server> servers = new ArrayList<>(); //多服务
		// 表示服务器地址或者URL模板列表，多个服务地址随时切换（只不过是有多台IP有当前的服务API）
		servers.add(new Server().url("http://localhost:8080").description("服务1"));
		servers.add(new Server().url("http://localhost:8081").description("服务2"));

		// // 设置 spring security apikey accessToken 认证的请求头 X-Token: xxx.xxx.xxx
		SecurityScheme securityScheme = new SecurityScheme()
				.name("x-token")
				.type(SecurityScheme.Type.APIKEY)
				.description("APIKEY认证描述")
				.in(SecurityScheme.In.HEADER);

		// 设置 spring security jwt accessToken 认证的请求头 Authorization: Bearer xxx.xxx.xxx
		SecurityScheme securityScheme1 = new SecurityScheme()
				.name("JWT认证")
				.scheme("bearer") //如果是Http类型，Scheme是必填
				.type(SecurityScheme.Type.HTTP)
				.description("认证描述")
				.in(SecurityScheme.In.HEADER)
				.bearerFormat("JWT");

		List<SecurityRequirement> securityRequirements = new ArrayList<>();

		SecurityRequirement securityRequirement = new SecurityRequirement();
		securityRequirement.addList("authScheme");

		securityRequirements.add(securityRequirement);

		// 返回信息
		return new OpenAPI()
				//.openapi("3.0.1")  // Open API 3.0.1(默认)
				.info(info)
				.servers(servers)
				.components(new Components().addSecuritySchemes("authScheme", securityScheme1)) //添加鉴权组件
				.security(securityRequirements) //全部添加鉴权小锁
				.externalDocs(new ExternalDocumentation()
						.description("对外说明") //对外说明
						.url("https://www.cnblogs.com/vic-tory/"));       // 配置Swagger3.0描述信息
	}
}
