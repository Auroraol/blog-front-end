package com.lfj.blog.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfj.blog.common.security.AuthUser;
import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.entity.User;
import com.lfj.blog.handler.security.UserContext;
import com.lfj.blog.mapper.UserMapper;
import com.lfj.blog.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
		return ResponseResult.tokenError();

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





