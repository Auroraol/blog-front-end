package cn.poile.ucs.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置文件, 写不需要认证的url
 * @Author: LFJ
 * @Date: 2024-03-10 22:57
 */
@Configuration
@ConfigurationProperties(prefix = "ignored")
@Data
public class IgnoredUrlsProperties {
	private List<String> urls = new ArrayList<>();
}
