package com.lfj.blog.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: LFJ
 * @Date: 2024-03-10 22:57
 */
@Configuration
@ConfigurationProperties(prefix = "ignored")
@Data
public class IgnoredUrlsProperties {
	private List<String> urls = new ArrayList<>();
}
