package com.lfj.blog.controller;


import com.lfj.blog.common.security.Token;
import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.service.UserService;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: LFJ
 * @Date: 2024-03-09 15:43
 */


@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;

//	@Autowired
//	private StringEncryptor stringEncryptor;

	@PostMapping("/login")
	public ResponseResult<Token> userLogin(@RequestParam String username, @RequestParam String password) {
		return userService.usernameLogin(username, password);
	}

//	@GetMapping("/test1")
//	public void encryptTest() {
//		String content = "741106";
//		String encryptStr = stringEncryptor.encrypt(content);
//		String decryptStr = stringEncryptor.decrypt(encryptStr);
//		System.out.println("加密后的内容：" + encryptStr);
//		System.out.println("解密后的内容：" + decryptStr);
//	}
}
