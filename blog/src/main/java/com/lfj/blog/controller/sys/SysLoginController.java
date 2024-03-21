package com.lfj.blog.controller.sys;

import com.lfj.blog.common.security.Token;
import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.service.sys.SysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: LFJ
 * @Date: 2024-03-13 18:22
 */
@RestController
@RequestMapping("/login")
public class SysLoginController {

	@Autowired
	SysLoginService sysLoginService;

	@PostMapping("/web")
	public ResponseResult<Token> userLogin(@RequestBody Map<String, String> loginData) {
		String username = loginData.get("username");
		String password = loginData.get("password");
		return sysLoginService.login(username, password);
	}
}
