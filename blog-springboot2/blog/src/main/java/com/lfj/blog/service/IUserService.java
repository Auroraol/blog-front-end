package com.lfj.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lfj.blog.common.security.details.vo.UserVo;
import com.lfj.blog.controller.model.request.UpdateUserRequest;
import com.lfj.blog.controller.model.request.UserRegisterRequest;
import com.lfj.blog.entity.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 16658
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-03-22 14:15:12
 */
public interface IUserService extends IService<User> {

	/**
	 * 根据用户名或手机号查询用户信息
	 *
	 * @param username
	 * @param mobile
	 * @return cn.poile.blog.entity.User
	 */
	UserVo selectUserVoByUsernameOtherwiseMobile(String username, Long mobile);


	/**
	 * 根据用户id获取用户
	 *
	 * @param id
	 * @return
	 */
	UserVo selectUserVoById(Integer id);


	/**
	 * 用户注册
	 *
	 * @param request
	 */
	void register(UserRegisterRequest request);

	/**
	 * 更新用户信息
	 *
	 * @param request
	 */
	void update(UpdateUserRequest request);

	/**
	 * 发送邮箱验证链接
	 *
	 * @param email
	 * @return void
	 */
	void validateEmail(String email);


	/**
	 * 绑定邮箱
	 *
	 * @param code
	 * @return void
	 */
	void bindEmail(String code);

	/**
	 * 更新头像
	 *
	 * @param file
	 * @return void
	 */
	void updateAvatar(MultipartFile file);

	/**
	 * 修改密码
	 *
	 * @param oldPassword
	 * @param newPassword
	 * @return void
	 */
	void updatePassword(String oldPassword, String newPassword);


	/**
	 * 重置密码
	 *
	 * @param mobile
	 * @param code
	 * @param password
	 * @return void
	 */
	void resetPassword(String mobile, String code, String password);

	/**
	 * 更换手机号  验证手机号
	 *
	 * @param mobile
	 * @param code
	 * @return void
	 */
	void validateMobile(long mobile, String code);

	/**
	 * 更换手机号 重新绑定
	 *
	 * @param mobile
	 * @param code
	 * @return void
	 */
	void rebindMobile(long mobile, String code);


	/**
	 * 绑定手机号 - 用于原手机号为空的情况
	 *
	 * @param mobile
	 * @param code
	 */
	void bindMobile(long mobile, String code);

	/**
	 * 绑定用户名 - 用于用户名为空的情况
	 *
	 * @param username
	 */
	void bindUsername(String username);


	/**
	 * 分页查询用户
	 *
	 * @param current
	 * @param size
	 * @param username
	 * @param nickname
	 * @return
	 */
	IPage<User> page(long current, long size, String username, String nickname);

	/**
	 * 修改用户状态
	 *
	 * @param userId
	 * @param status
	 */
	void status(Integer userId, Integer status);
//

}
