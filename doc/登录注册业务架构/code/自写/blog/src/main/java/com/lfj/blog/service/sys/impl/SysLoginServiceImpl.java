package com.lfj.blog.service.sys.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lfj.blog.common.cache.CachePrefix;
import com.lfj.blog.common.security.AuthUser;
import com.lfj.blog.common.security.Token;
import com.lfj.blog.common.security.UserEnums;
import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.entity.User;
import com.lfj.blog.service.user.UserService;
import com.lfj.blog.service.sys.SysLoginService;
import com.lfj.blog.utils.token.SecurityKey;
import com.lfj.blog.utils.token.TokenUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: LFJ
 * @Date: 2024-03-13 18:24
 */
@Service
public class SysLoginServiceImpl implements SysLoginService {
	@Autowired
	UserService userService;

	@Autowired
	private StringRedisTemplate redisTemplate;


	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("123456"));
	}
	@Override
	public ResponseResult<Token> login(String username, String password) {
		/**
		 1. 根据用户名查找Member信息
		 2. 如果为null，就是用户不存在
		 3. 密码进行匹配，如果不匹配 密码不正确
		 4. jwt 生成token
		 5. jwt 生成token, token放入redis当中，accessToken 过期短， refreshToken 过期长
		 **/
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
				.eq(User::getUsername, username);
		List<User> userList = userService.list(queryWrapper);

		if (ObjectUtils.isEmpty(userList)) {
			return ResponseResult.accountNotFoundError();
		}

		User user = userList.get(0);
		//用的security的密码类
		if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
			return ResponseResult.accountError();
		}

		//一般会登录的时候，记录 用户的最后一次登录时间
		//MQ 考虑使用mq 把信息发到mq当中，由mq的消费者 来去更新1
		user.setRecentlylanded(LocalDateTime.now());
		userService.updateById(user);

		Token token = genToken(user);
		return ResponseResult.success(token);
	}


	private Token genToken(User user) {
		Token token = new Token();
		// token存放信息
		AuthUser authUser = new AuthUser(user.getUsername(), String.valueOf(user.getId()),
				user.getUsername(), UserEnums.USER);
		// 7天(一般过期时间设置成30分钟)
//		String jwtAccessToken = TokenUtils.createToken(user.getUsername(), authUser, 7 * 24 * 60 * 60 * 1000L);
		String jwtAccessToken = TokenUtils.createToken(user.getUsername(), authUser,  10 * 1000L);

		token.setAccessToken(jwtAccessToken);
//		redisTemplate.opsForValue().set(CachePrefix.ACCESS_TOKEN.name() + UserEnums.USER.name() + jwtAccessToken
//				,"1", 7, TimeUnit.DAYS); // 储存到Redis中 前缀+用户类型+jwtToken
		redisTemplate.opsForValue().set(CachePrefix.ACCESS_TOKEN.name() + UserEnums.USER.name() + jwtAccessToken
				,"1", 10, TimeUnit.SECONDS); // 储存到Redis中 前缀+用户类型+jwtToken

		// 15天
		//设置刷新token，当accessToken过期的时候，可以通过refreshToken来 重新获取accessToken 而不用访问数据库
		String jwtRefreshToken = TokenUtils.createToken(user.getUsername(), authUser, 15 * 24 * 60 * 60 * 1000L);
		token.setRefreshToken(jwtRefreshToken);
		redisTemplate.opsForValue().set(CachePrefix.REFRESH_TOKEN.name() + UserEnums.USER.name() + jwtRefreshToken
				,"1", 15, TimeUnit.DAYS);

		return token;
	}
}
