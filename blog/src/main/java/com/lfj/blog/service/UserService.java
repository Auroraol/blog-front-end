package com.lfj.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lfj.blog.common.security.Token;
import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.entity.User;


/**
 * @author 16658
 * @description 针对表【user】的数据库操作Service
 * @createDate 2024-03-09 17:46:34
 */
public interface UserService extends IService<User> {
	ResponseResult<Token> usernameLogin(String username, String password);
}
