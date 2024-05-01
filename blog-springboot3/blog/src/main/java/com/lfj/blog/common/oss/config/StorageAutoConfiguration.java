package com.lfj.blog.common.oss.config;

import com.lfj.blog.common.oss.Storage;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * oss 存储自动配置
 **/
@Log4j2
@Configuration
@EnableConfigurationProperties({StorageProperties.class})
public class StorageAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(name = {"storage"})
//ConditionalOnMissingBean表示 只有当名为 "storage" 的 Bean 不存在时，才会创建当前的 Bean。确保在容器中只会有一个名为 "storage" 的 Bean
	public Storage storage(StorageProperties properties) {
		return StorageFactory.build(properties);
	}
}

// Configuration + Bean进行Bean注册