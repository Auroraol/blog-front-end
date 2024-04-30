package com.lfj.blog.service.security.impl;


import com.lfj.blog.common.security.MobileCodeAuthenticationToken;
import com.lfj.blog.common.security.token.AuthenticationToken;
import com.lfj.blog.common.security.token.RedisTokenStore;
import com.lfj.blog.common.sms.service.SmsCodeService;
import com.lfj.blog.entity.Client;
import com.lfj.blog.service.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * 2019-10-28 18:27
 **/
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private RedisTokenStore redisTokenStore;

	@Autowired
	private SmsCodeService smsCodeService;

	/**
	 * 用户名或手机号密码认证
	 *
	 * @param s        手机号或用户名
	 * @param password 密码
	 * @param client
	 * @return
	 */
	@Override
	public AuthenticationToken usernameOrMobilePasswordAuthenticate(String s, String password, Client client) {

		// 构造用户名密码认证信息
		// 将用户名和密码放入，然后此方法会调用我们之前写的UserDetailsService的实现类中UserDetailsServiceImpl中的方法进行校验
		// （PS： 在后面JwtAuthenticationTokenFilter类中使用此UsernamePasswordAuthenticationToken的时候，构造的入参也可以放（用户LoginUser）类型
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(s, password);
		// 对认证信息进行认证, AuthenticationManager的authenticate()方法来进行用户认证
		try {

			Authentication authenticate = authenticationManager.authenticate(authenticationToken);
			// 将认证信息存储在 redisTokenStore, 返回AuthenticationToken实体类
			return redisTokenStore.storeToken(authenticate, client);
		} catch (AuthenticationException e) {
			// 认证失败，抛出 BadCredentialsException
			throw new BadCredentialsException("用户名或密码错误");
		}

		// 账户是否未过期、未被锁定、密码未过期以及是否启用 返回提示信息


//		Authentication authenticate = authenticationManager.authenticate(authenticationToken);
//		if (Objects.isNull(authenticate)) {
//			throw new RuntimeException("用户名或密码错误");
//		}
//		// 将认证信息存储在 redisTokenStore, 返回AuthenticationToken实体类
//		return redisTokenStore.storeToken(authenticate, client);
	}

	/**
	 * 手机号验证码认证
	 *
	 * @param mobile 手机号
	 * @param code   短信验证码
	 * @param client 客户端
	 * @return
	 */
	@Override
	public AuthenticationToken mobileCodeAuthenticate(String mobile, String code, Client client) {
		// 自定义认证器此方法会调用我们之前写的UserDetailsService的实现类中UserDetailsServiceImpl中的方法进行校验
		MobileCodeAuthenticationToken authenticationToken = new MobileCodeAuthenticationToken(mobile, code);
		// 对认证信息进行认证, AuthenticationManager的authenticate()方法来进行用户认证
		Authentication authenticate = authenticationManager.authenticate(authenticationToken);

		// 将认证信息存储在 redisTokenStore, 返回AuthenticationToken实体类
		AuthenticationToken storeAccessToken = redisTokenStore.storeToken(authenticate, client);
		smsCodeService.deleteSmsCode(mobile); // 删除验证码
		return storeAccessToken;
	}

	/**
	 * 移除 accessToken 相关
	 *
	 * @param accessToken
	 * @param client      客户端
	 */
	@Override
	public void remove(String accessToken, Client client) {
		redisTokenStore.remove(accessToken, client);  // redss删除用户数据
	}

	/**
	 * 刷新accessToken
	 *
	 * @param refreshToken
	 * @param client       客户端
	 * @return
	 */
	@Override
	public AuthenticationToken refreshAccessToken(String refreshToken, Client client) {
		return redisTokenStore.refreshAuthToken(refreshToken, client);
	}


}
