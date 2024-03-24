package com.lfj.blog.service.sys.security.impl;


import com.lfj.blog.common.security.AuthenticationToken;
import com.lfj.blog.common.security.MobileCodeAuthenticationToken;
import com.lfj.blog.common.security.RedisTokenStore;
import com.lfj.blog.common.sms.service.SmsCodeService;
import com.lfj.blog.entity.Client;
import com.lfj.blog.service.sys.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * @author: yaohw
 * @create: 2019-10-28 18:27
 **/
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private RedisTokenStore tokenStore;

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
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(s, password);
		Authentication authenticate = authenticationManager.authenticate(authenticationToken);
		return tokenStore.storeToken(authenticate, client);
	}

	/**
	 * 手机号验证码认证
	 *
	 * @param mobile
	 * @param code
	 * @param client 客户端
	 * @return
	 */
	@Override
	public AuthenticationToken mobileCodeAuthenticate(String mobile, String code, Client client) {
		MobileCodeAuthenticationToken authenticationToken = new MobileCodeAuthenticationToken(mobile, code);
		Authentication authenticate = authenticationManager.authenticate(authenticationToken);
		AuthenticationToken storeAccessToken = tokenStore.storeToken(authenticate, client);
		smsCodeService.deleteSmsCode(mobile);
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
		tokenStore.remove(accessToken, client);
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
		return tokenStore.refreshAuthToken(refreshToken, client);
	}


}
