package com.lfj.blog.service.sys.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lfj.blog.common.cache.CachePrefix;
import com.lfj.blog.common.security.AuthUser;
import com.lfj.blog.common.security.Token;
import com.lfj.blog.common.security.UserEnums;
import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.service.sys.SysAuthService;
import com.lfj.blog.utils.token.SecurityKey;
import com.lfj.blog.utils.token.TokenUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Author: LFJ
 * @Date: 2024-03-20 15:51
 */

@Service
public class SysAuthServicelmpl implements SysAuthService {
	@Autowired
	private StringRedisTemplate redisTemplate;


	/**
	 * 刷新token
	 * @param refreshToken
	 * @return
	 */
	@Override
	public ResponseResult<Object> refreshToken(String refreshToken) {
		Claims claim = TokenUtils.parserToken(refreshToken);
		if (claim == null){
			return ResponseResult.tokenError();
		}
		AuthUser authUser = JSON.parseObject(claim.get(SecurityKey.USER_CONTEXT).toString(), AuthUser.class);

		String value = redisTemplate.opsForValue().get(CachePrefix.REFRESH_TOKEN.name() + UserEnums.USER.name() + refreshToken);
		if (StringUtils.isBlank(value)){
			return ResponseResult.tokenError();
		}
		Token token = new Token();
		String accessToken = TokenUtils.createToken(authUser.getUsername(), authUser, 7 * 24 * 60L);
		redisTemplate.opsForValue().set(CachePrefix.ACCESS_TOKEN.name() + UserEnums.USER.name() + accessToken, "true",7, TimeUnit.DAYS);	//放入redis当中

		String newRefreshToken = TokenUtils.createToken(authUser.getUsername(), authUser, 15 * 24 * 60L);
		redisTemplate.opsForValue().set(CachePrefix.REFRESH_TOKEN.name() + UserEnums.USER.name() + refreshToken, "true",15, TimeUnit.DAYS);

		token.setAccessToken(accessToken);
		token.setRefreshToken(newRefreshToken);
		return ResponseResult.success(token);
	}
}