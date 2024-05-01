package com.lfj.blog.common.security.oauth;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import com.lfj.blog.exception.ApiException;
import com.lfj.blog.utils.HttpClientUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * gitee 第三方登录
 **/
@Log4j2
@Service
public class GiteeThirdAuth {

	@Value("${oauth.gitee.clientId}")
	public String clientId;

	@Value("${oauth.gitee.clientSecret}")
	public String clientSecret;

	@Value("${oauth.gitee.redirect_uri}")
	public String redirect;

	/**
	 * 获取第三方用户信息
	 *
	 * @param accessToken
	 * @return
	 */
	public ThirdAuthUser getUserInfoByToken(String accessToken) {
		Map<String, String> params = new HashMap<>(2);
		params.put("access_token", accessToken);
		String result = HttpClientUtil.doGet(OauthConstant.GITEE_ACCESS_USER_URL, params);
		if (StringUtils.isBlank(result)) {
			throw new ApiException(ResponseCodeEnum.SYSTEM_ERROR.getCode(), "获取第三方token出错");
		}
		JSONObject jsonObject = JSON.parseObject(result);
		ThirdAuthUser thirdAuthUser = new ThirdAuthUser();
		thirdAuthUser.setUuid(jsonObject.getString("id"));
		thirdAuthUser.setNickname(jsonObject.getString("name"));
		thirdAuthUser.setAvatar(jsonObject.getString("avatar_url"));
		thirdAuthUser.setEmail(jsonObject.getString("email"));
		return thirdAuthUser;
	}

	/**
	 * 获取第三方用户信息
	 *
	 * @param code
	 * @return
	 */
	public ThirdAuthUser getUserInfoByCode(String code) {
		ThirdAuthToken authToken = getAuthToken(code); //获取第三方token信息
		return getUserInfoByToken(authToken.getAccessToken());
	}

	/**
	 * 获取第三方token信息
	 *
	 * @param code
	 * @return
	 */
	public ThirdAuthToken getAuthToken(String code) {
		//1. 创建http请求，构建请求体和请求url等, 并向gitee发起请求
		Map<String, String> params = new HashMap<>(8);
		params.put("code", code);
		params.put("client_id", clientId);
		params.put("client_secret", clientSecret);
		params.put("grant_type", "authorization_code");
		params.put("redirect_uri", redirect);
		String result = HttpClientUtil.doPost(OauthConstant.GITEE_ACCESS_TOKE_URL, params);
		//2. 获取gitee对应的响应消息，根据消息解析出用户的 access token
		if (StringUtils.isBlank(result)) {
			throw new ApiException(ResponseCodeEnum.SYSTEM_ERROR.getCode(), "获取第三方token出错");
		}
		JSONObject jsonObject = JSON.parseObject(result);
		ThirdAuthToken thirdAuthToken = new ThirdAuthToken();
		thirdAuthToken.setAccessToken(jsonObject.getString("access_token"));
		return thirdAuthToken;
	}
}
