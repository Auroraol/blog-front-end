package com.lfj.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfj.blog.common.constant.RoleConstant;
import com.lfj.blog.common.constant.UserConstant;
import com.lfj.blog.common.oss.Storage;
import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import com.lfj.blog.common.security.ServerSecurityContext;
import com.lfj.blog.common.security.details.CustomUserDetails;
import com.lfj.blog.common.security.details.vo.UserVo;
import com.lfj.blog.common.security.token.AuthenticationToken;
import com.lfj.blog.common.security.token.RedisTokenStore;
import com.lfj.blog.common.sms.service.SmsCodeService;
import com.lfj.blog.controller.model.request.UpdateUserRequest;
import com.lfj.blog.controller.model.request.UserRegisterRequest;
import com.lfj.blog.entity.User;
import com.lfj.blog.exception.ApiException;
import com.lfj.blog.mapper.UserMapper;
import com.lfj.blog.service.IUserService;
import com.lfj.blog.service.security.biz.EmailService;
import com.lfj.blog.utils.RandomValueStringGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 16658
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-03-22 14:15:12
 */
@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User>
		implements IUserService {

	/**
	 * 邮箱绑定code的redis前缀
	 */
	private final static String REDIS_MAIL_CODE_PREFIX = "mail:code:";
	/**
	 * 更换手机号 手机号验证redis前缀
	 */
	private final static String REDIS_MOBILE_VALIDATED_PREFIX = "mobile:validated:";

	@Value("${mail.check}")
	private String prefix;

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private SmsCodeService smsCodeService;

	@Autowired
	private RedisTokenStore tokenStore;  // 处理token

	@Autowired
	private Storage storage;   // oss服务

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 根据用户名查询
	 * username 不为空时使用username查询，否则使用mobile查询
	 *
	 * @param username
	 * @return cn.poile.blog.entity.User
	 */
	@Override
	public UserVo selectUserVoByUsernameOtherwiseMobile(String username, Long mobile) {
		User user = selectUserByUsernameOtherwiseMobile(username, mobile);
		if (user == null) {
			return null;
		}
		UserVo userVo = new UserVo();
		BeanUtils.copyProperties(user, userVo);
		Integer admin = user.getAdmin();
//		System.out.println("");
		List<String> roleList = new ArrayList<>();
		roleList.add(admin.equals(UserConstant.ADMIN) ? RoleConstant.ADMIN : RoleConstant.ORDINARY);
		userVo.setRoles(roleList);
		return userVo;
	}

	/**
	 * 根据用户id获取userVo
	 *
	 * @param id
	 * @return
	 */
	@Override
	@Cacheable(value = "user", key = "#id")
	public UserVo selectUserVoById(Integer id) {
		User user = getById(id);
		if (user == null) {
			return null;
		}
		UserVo userVo = new UserVo();
		BeanUtils.copyProperties(user, userVo);
		Integer admin = user.getAdmin();
		List<String> roleList = new ArrayList<>();
		roleList.add(admin.equals(UserConstant.ADMIN) ? RoleConstant.ADMIN : RoleConstant.ORDINARY);
		userVo.setRoles(roleList);
		return userVo;
	}

	/**
	 * 用户注册
	 *
	 * @param request
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)  // 开启事务
	public void register(UserRegisterRequest request) {
		String mobile = request.getMobile();
		String code = request.getCode();
		checkSmsCode(mobile, code); // 检测手机号和验证码是否对
		String username = request.getUsername();
		User userDao = selectUserByUsernameOrMobile(username, Long.valueOf(mobile));
		if (userDao != null && username.equals(userDao.getUsername())) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "用户名已存在");
		}
		if (userDao != null && Long.valueOf(mobile).equals(userDao.getMobile())) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "手机号已被使用");
		}
		// 保存数据库
		User user = new User();
		user.setUsername(username);
		// 密码加密保存
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();  // 使用spring security 加密密码
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setMobile(Long.valueOf(mobile));
		String suffix = mobile.substring(5);
		user.setNickname("用户" + suffix);
		user.setGender(UserConstant.GENDER_MALE);
		user.setBirthday(LocalDate.now());
		user.setStatus(UserConstant.STATUS_NORMAL);
		user.setCreateTime(LocalDateTime.now());
		user.setAdmin(UserConstant.ORDINARY);  //默认普通用户
		save(user);
		smsCodeService.deleteSmsCode(mobile); //删除验证码缓存
	}

	/**
	 * 发送邮箱验证链接
	 *
	 * @param email
	 * @return void
	 */
	@Override
	public void validateEmail(String email) {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(User::getEmail, email);
		long count = count(queryWrapper);
		if (count != 0) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "邮箱已被使用,请换个绑定");
		}
		// 请求携带token, 可以用任务上下文获得authenticationToken
		AuthenticationToken authenticationToken = ServerSecurityContext.getAuthenticationToken(true);
		Map<String, Object> params = new HashMap<>(1);
		// 生成模板参数
		RandomValueStringGenerator generator = new RandomValueStringGenerator();
		String code = generator.generate();
		String accessToken = authenticationToken.getAccessToken();
		String checkUrl = prefix + "?code=" + code;
		params.put("checkUrl", checkUrl);
		// 服务端生成Redis缓存
		String key = REDIS_MAIL_CODE_PREFIX + code;
		Map<String, String> map = new HashMap<>(2);
		map.put("access_token", accessToken);
		map.put("email", email);
		stringRedisTemplate.opsForHash().putAll(key, map);
		stringRedisTemplate.expire(key, 2L, TimeUnit.HOURS);
		// 发送邮箱
		emailService.asyncSendHtmlMail(email, "邮箱验证", "email", params);
	}

	/**
	 * 绑定邮箱
	 *
	 * @param code
	 * @return void
	 */
	@Override
	public void bindEmail(String code) {
		Map<Object, Object> resultMap = stringRedisTemplate.opsForHash().
				entries(REDIS_MAIL_CODE_PREFIX + code);
		if (resultMap.isEmpty()) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "code无效或code已过期");
		}
		String accessToken = (String) resultMap.get("access_token");
		String email = (String) resultMap.get("email");
		stringRedisTemplate.delete(REDIS_MAIL_CODE_PREFIX + code);
		// 请求没有直接携带token, 不能用任务上下文
		AuthenticationToken authToken = tokenStore.readByAccessToken(accessToken);
		if (authToken == null) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "code无效或code已过期");
		}
		// 从Redis中得到保存的用户信息数据
		CustomUserDetails principal = authToken.getPrincipal(); // 得到用户数据, 自定义AuthenticationToken中Principal属性保存着对应的用户数据
		User user = new User();
		Integer userId = principal.getId();
		user.setId(userId);
		user.setEmail(email);  // 更新邮箱
		// 数据库数据更新
		updateById(user);
		// 清空用户缓存
		tokenStore.clearUserCacheById(userId);
	}

	/**
	 * 更新用户信息
	 *
	 * @param request
	 */
	@Override
	public void update(UpdateUserRequest request) {
		CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
		if (!request.getUserId().equals(userDetail.getId())) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "用户id跟当前用户id不匹配");
		}
		User user = new User();
		BeanUtils.copyProperties(request, user);
		Integer userId = request.getUserId();
		user.setId(userId);
		updateById(user);

		tokenStore.clearUserCacheById(userId);
	}


	/**
	 * 更新头像
	 *
	 * @param file
	 * @return void
	 */
	@Override
	public void updateAvatar(MultipartFile file) {
		String filename = file.getOriginalFilename();
		String contentType = file.getContentType();
		String extension = filename.substring(filename.lastIndexOf(".") + 1);
		String name = System.currentTimeMillis() + "." + extension;
		try {
			// 调用上传头像服务
			String fullPath = storage.upload(file.getInputStream(), name, contentType);
			CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
			User user = new User();
			Integer userId = userDetail.getId();
			user.setId(userId);
			user.setAvatar(fullPath);
			updateById(user);
			// 删除原头像文件
			storage.delete(userDetail.getAvatar());

			// 清空用户缓存
			tokenStore.clearUserCacheById(userId);
		} catch (IOException e) {
			log.error("上传文件失败:{0}", e);
			throw new ApiException(ResponseCodeEnum.SYSTEM_ERROR.getCode(), ResponseCodeEnum.SYSTEM_ERROR.getMessage());
		}
	}

	/**
	 * 修改密码
	 *
	 * @param oldPassword
	 * @param newPassword
	 * @return void
	 */
	@Override
	public void updatePassword(String oldPassword, String newPassword) {
		CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		boolean matches = passwordEncoder.matches(oldPassword, userDetail.getPassword());
		if (!matches) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "原密码不正确");
		}
		User user = new User();
		Integer userId = userDetail.getId();
		user.setId(userId);
		String encodePassword = passwordEncoder.encode(newPassword);
		user.setPassword(encodePassword);
		updateById(user);
		// 清空用户缓存
		tokenStore.clearUserCacheById(userId);
	}


	/**
	 * 重置密码
	 *
	 * @param mobile
	 * @param code
	 * @param password
	 * @return void
	 */
	@Override
	public void resetPassword(String mobile, String code, String password) {
		// 判断用户是否存在
		UserVo userVo = selectUserVoByUsernameOtherwiseMobile(null, Long.valueOf(mobile));
		if (userVo == null) {
			throw new ApiException(ResponseCodeEnum.ACCOUNT_NOT_FOUND.getCode(), ResponseCodeEnum.ACCOUNT_NOT_FOUND.getMessage());
		}
		// 验证码校验
		checkSmsCode(mobile, code);
		CustomUserDetails userDetails = new CustomUserDetails();
		BeanUtils.copyProperties(userVo, userDetails);
		User newUser = new User();
		Integer userId = userDetails.getId();
		newUser.setId(userId);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodePassword = passwordEncoder.encode(password);
		newUser.setPassword(encodePassword);
		updateById(newUser);

		// 清空用户缓存
		tokenStore.clearUserCacheById(userId);
	}

	/**
	 * 更换手机号  验证手机号
	 *
	 * @param mobile
	 * @param code
	 * @return void
	 */
	@Override
	public void validateMobile(long mobile, String code) {
		checkSmsCode(mobile, code);
		CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
		if (!userDetail.getMobile().equals(mobile)) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "手机号与当前用户手机号不匹配");
		}
		// 经过原手机号验证标识
		stringRedisTemplate.opsForValue().set(REDIS_MOBILE_VALIDATED_PREFIX + mobile, Long.toString(mobile), 5L, TimeUnit.MINUTES);
		smsCodeService.deleteSmsCode(String.valueOf(mobile));
	}

	/**
	 * 更换手机号 重新绑定
	 *
	 * @param mobile
	 * @param code
	 * @return void
	 */
	@Override
	public void rebindMobile(long mobile, String code) {
		CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
		long cacheKey = userDetail.getMobile();
		// 判断是否经过步骤一
		String validated = stringRedisTemplate.opsForValue().get(REDIS_MOBILE_VALIDATED_PREFIX + cacheKey);
		if (StringUtils.isBlank(validated)) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "未经原手机号验证或验证已超时，请验证原手机号通过后再试");
		}
		// 验证码校验
		checkSmsCode(mobile, code);
		// 判断手机号是否已被注册
		User user = selectUserByUsernameOrMobile(null, mobile);
		if (user != null) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(),
					ResponseCodeEnum.INVALID_REQUEST.getMessage());
		}
		User newUser = new User();
		Integer userId = userDetail.getId();
		newUser.setId(userId);
		newUser.setMobile(mobile);
		updateById(newUser);
		// 清空用户缓存
		tokenStore.clearUserCacheById(userId);
		smsCodeService.deleteSmsCode(String.valueOf(mobile));
	}


	/**
	 * 绑定手机号 - 用于原手机号为空的情况下
	 *
	 * @param mobile
	 * @param code
	 */
	@Override
	public void bindMobile(long mobile, String code) {
		CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
		boolean hasMobile = userDetail.getMobile() != null;
		if (hasMobile) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "已存在手机号，请验证原手机后重新绑定");
		}
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(User::getMobile, mobile);
		long count = count(queryWrapper);
		if (count != 0) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "手机号已被使用");
		}
		// 验证码校验
		checkSmsCode(mobile, code);
		User user = new User();
		Integer id = userDetail.getId();
		user.setId(id);
		user.setMobile(mobile);
		updateById(user);
		smsCodeService.deleteSmsCode(String.valueOf(mobile));
		// 清空用户缓存
		tokenStore.clearUserCacheById(id);
	}

	/**
	 * 绑定用户名 - 用于用户名为空的情况
	 *
	 * @param username
	 */
	@Override
	public void bindUsername(String username) {
		CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(User::getUsername, username);
		long count = count(queryWrapper);
		if (count != 0) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "用户名已存在");
		}
		User updateUser = new User();
		updateUser.setId(userDetail.getId());
		updateUser.setUsername(username);
		updateById(updateUser);
		// 清空用户缓存
		tokenStore.clearUserCacheById(userDetail.getId());
	}


	/**
	 * 校验短信验证码
	 *
	 * @param mobile
	 * @param code
	 */
	private void checkSmsCode(long mobile, String code) {
		if (!smsCodeService.checkSmsCode(String.valueOf(mobile), code)) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "验证码不正确");
		}
	}

	
	/**
	 * 分页查询用户
	 *
	 * @param current
	 * @param size
	 * @param username
	 * @param nickname
	 * @return
	 */
	@Override
	public IPage<User> page(long current, long size, String username, String nickname) {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(username)) {
			queryWrapper.lambda().like(User::getUsername, username);
		}
		if (StringUtils.isNotBlank(nickname)) {
			queryWrapper.lambda().like(User::getNickname, nickname);
		}
		return page(new Page<>(current, size), queryWrapper);
	}

	/**
	 * 修改用户状态
	 *
	 * @param userId
	 * @param status
	 */
	@Override
	public void status(Integer userId, Integer status) {
		// 状态，0：正常，1：锁定，2：禁用，3：过期
		int min = 0;
		int max = 3;
		if (status < min || status > max) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "无效状态值");
		}
		User daoUser = getById(userId);
		if (daoUser == null) {
			throw new ApiException(ResponseCodeEnum.ACCOUNT_NOT_FOUND.getCode(), "用户不存在");
		}
		// 数据库数据更新
		User user = new User();
		user.setId(userId);
		user.setStatus(status);
		updateById(user);

		// 清空用户缓存
		tokenStore.clearUserCacheById(userId);
	}

	/**
	 * 校验短信验证码
	 *
	 * @param mobile
	 * @param code
	 */
	private void checkSmsCode(String mobile, String code) {
		if (!smsCodeService.checkSmsCode(mobile, code)) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "验证码不正确");
		}
	}

	/**
	 * 根据用户名或手机号查询 User
	 *
	 * @param username
	 * @param mobile
	 * @return
	 */
	private User selectUserByUsernameOrMobile(String username, Long mobile) {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(User::getUsername, username).or().eq(User::getMobile, mobile);
		return getOne(queryWrapper, false);
	}

	/**
	 * username 不为空时使用username查询，否则使用mobile查询
	 *
	 * @param username
	 * @param mobile
	 * @return
	 */
	private User selectUserByUsernameOtherwiseMobile(String username, Long mobile) {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(username)) {
			queryWrapper.lambda().eq(User::getUsername, username);
		} else {
			queryWrapper.lambda().eq(User::getMobile, mobile);
		}
		return getOne(queryWrapper, false);
	}

}




