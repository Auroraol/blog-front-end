package com.lfj.blog.service.sys;

import com.lfj.blog.common.security.Token;
import com.lfj.blog.common.vo.ResponseResult;

/**
 * @Author: LFJ
 * @Date: 2024-03-13 18:23
 */
public interface  SysLoginService {
	ResponseResult<Token> login(String username, String password);


}
