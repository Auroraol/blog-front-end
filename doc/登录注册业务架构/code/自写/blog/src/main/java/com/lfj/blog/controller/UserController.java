package com.lfj.blog.controller;


import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.entity.User;
import com.lfj.blog.service.user.UserService;
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

	@GetMapping("/info")
	public ResponseResult<User> getUserInfo() {
		return userService.getUserInfo();
	}


	@GetMapping("/nickName")
	public ResponseResult<String> updateNickName(
		@RequestParam("userName") String userName,
		@RequestParam("nickName") String nickName) {
		return userService.updateTruename(userName, nickName);
	}
}
