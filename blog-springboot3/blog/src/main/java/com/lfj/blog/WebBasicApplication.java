package com.lfj.blog;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableCaching   //开启spring缓存
@EnableEncryptableProperties  //开启自动解密功能
@SpringBootApplication
public class WebBasicApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebBasicApplication.class, args);
	}
}
