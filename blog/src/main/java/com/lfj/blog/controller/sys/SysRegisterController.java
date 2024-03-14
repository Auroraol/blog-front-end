package com.lfj.blog.controller.sys;

import com.lfj.blog.common.security.Token;
import com.lfj.blog.common.vo.RegisterInfo;
import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.service.sys.SysLoginService;
import com.lfj.blog.service.sys.SysRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: LFJ
 * @Date: 2024-03-14 21:14
 */

@RestController
@RequestMapping("/register")
public class SysRegisterController {
	@Autowired
	SysRegisterService sysRegisterService;

	@PostMapping("/web")
	public ResponseResult<String> userRegister(@RequestBody RegisterInfo registerInfo) {
		return sysRegisterService.register(registerInfo);
	}
}
