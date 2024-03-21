package com.lfj.blog.service.sys;

import com.lfj.blog.common.vo.ResponseResult;

/**
 * @Author: LFJ
 * @Date: 2024-03-20 15:51
 */
public interface SysAuthService {
	ResponseResult<Object> refreshToken(String refreshToken);
}
