package com.lfj.blog;

import com.lfj.blog.handler.security.AuthenticationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebBasicApplication  {


	public static void main(String[] args) {
		SpringApplication.run(WebBasicApplication.class, args);
	}
}
