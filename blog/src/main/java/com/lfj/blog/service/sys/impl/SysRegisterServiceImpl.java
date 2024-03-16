package com.lfj.blog.service.sys.impl;

import com.lfj.blog.common.vo.RegisterInfo;
import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.entity.User;
import com.lfj.blog.service.user.UserService;
import com.lfj.blog.service.sys.SysRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @Author: LFJ
 * @Date: 2024-03-14 21:22
 */

@Service
public class SysRegisterServiceImpl implements SysRegisterService {

	@Autowired
	UserService userService;

	@Override
	public ResponseResult<String> register(RegisterInfo registerInfo) {
		String name = registerInfo.getUsername();
		// 如果用户名不存在, 则用户注册
		if (ObjectUtils.isEmpty(userService.selectAllByUsername(name))){
			User user = new User();
			// 密码加密保存
			user.setPassword(new BCryptPasswordEncoder().encode(registerInfo.getPassword()));
			user.setUsername(name);
			userService.insertSelective(user);
		}
		else {
			return ResponseResult.accountRegisteredError();
		}
		return ResponseResult.success("success");
	}
}
