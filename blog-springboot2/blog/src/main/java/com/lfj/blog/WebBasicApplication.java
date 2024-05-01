package com.lfj.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableCaching   //开启spring缓存
@SpringBootApplication
public class WebBasicApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebBasicApplication.class, args);
	}
}
