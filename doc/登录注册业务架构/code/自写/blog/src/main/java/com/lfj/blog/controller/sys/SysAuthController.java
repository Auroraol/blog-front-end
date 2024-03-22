package com.lfj.blog.controller.sys;

import com.lfj.blog.common.security.RefreshToken;
import com.lfj.blog.common.security.RefreshToken;
import com.lfj.blog.common.security.Token;
import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.service.sys.SysAuthService;
import com.lfj.blog.service.sys.SysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: LFJ
 * @Date: 2024-03-13 18:22
 */
@RestController
@RequestMapping("/auth")
public class SysAuthController {

	@Autowired
	SysAuthService sysAuthService;


	@PostMapping("/refresh")
	public ResponseResult<Object> refreshToken(@RequestBody RefreshToken refreshToken) {
		return this.sysAuthService.refreshToken(refreshToken.getRefreshToken());
	}
}
