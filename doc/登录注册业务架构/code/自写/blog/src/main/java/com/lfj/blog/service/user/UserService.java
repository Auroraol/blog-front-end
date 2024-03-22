package com.lfj.blog.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lfj.blog.common.security.Token;
import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author 16658
 * @description 针对表【user】的数据库操作Service
 * @createDate 2024-03-09 17:46:34
 */
public interface UserService extends IService<User> {

	ResponseResult<User> getUserInfo();

	int insertSelective(User user);

	List<User> selectAllByUsername(@Param("username") String username);

	// 更新nickname
	ResponseResult<String> updateTruename(@Param("username") String username, @Param("nickName") String nickName);

}
