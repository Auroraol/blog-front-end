package com.lfj.blog.controller;


import com.lfj.blog.common.security.Token;
import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.service.UserService;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: LFJ
 * @Date: 2024-03-09 15:43
 */


@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;

	@PostMapping("/login")
//	public ResponseResult<Token> userLogin(@RequestParam String username, @RequestParam String password) {
//		return userService.usernameLogin(username, password);
//	}
	public ResponseResult<Token> userLogin(@RequestBody Map<String, String> loginData) {
		String username = loginData.get("username");
		String password = loginData.get("password");
		return userService.usernameLogin(username, password);
	}


}
