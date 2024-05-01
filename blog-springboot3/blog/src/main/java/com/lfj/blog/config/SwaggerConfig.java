package com.lfj.blog.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI swaggerOpenApi() {
		return new OpenAPI()
				.info(new Info().title("XXX平台YYY微服务")
						.description("描述平台多牛逼")
						.version("v1.0.0"))
				.externalDocs(new ExternalDocumentation()
						.description("设计文档")
						.url("https://juejin.cn/user/254742430749736/posts"));
	}
}
