package com.lfj.blog;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebBasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebBasicApplication.class, args);
	}
}
