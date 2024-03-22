package com.lfj.blog.service.user.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfj.blog.common.cache.CachePrefix;
import com.lfj.blog.common.security.AuthUser;
import com.lfj.blog.common.security.Token;
import com.lfj.blog.common.security.UserEnums;
import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.entity.User;
import com.lfj.blog.handler.security.UserContext;
import com.lfj.blog.mapper.UserMapper;
import com.lfj.blog.service.user.UserService;
import com.lfj.blog.utils.token.SecurityKey;
import com.lfj.blog.utils.token.TokenUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 16658
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-03-09 17:46:34
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
		implements UserService {

	@Autowired
	UserMapper userMapper;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Override
	public ResponseResult<User> getUserInfo() {

		AuthUser currentUser = UserContext.getCurrentUser();
		if (currentUser != null){
			String id = currentUser.getId();
			LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
					.eq(User::getId,id);
			List<User> userList = this.list(queryWrapper);
			if (userList.get(0) == null){
				return null;
			}
			return ResponseResult.success(userList.get(0));
		}
		//返回 登录已过期
		return ResponseResult.noLogin();

	}

	@Override
	public int insertSelective(User user) {
		return userMapper.insertSelective(user);
	}

	@Override
	public List<User> selectAllByUsername(String username) {
		return userMapper.selectAllByUsername(username);
	}

	@Override
	public ResponseResult<String> updateTruename(String userName, String nickName) {

			log.info(userName + "---" + nickName);
			if (userMapper.updateNicknameByUsername(nickName, userName) == 0){
				return  ResponseResult.account_updatetnickname_error();
			}
		   return ResponseResult.success("success");
	}
}





