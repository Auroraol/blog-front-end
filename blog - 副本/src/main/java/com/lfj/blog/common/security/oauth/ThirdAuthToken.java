package com.lfj.blog.common.security.oauth;

import lombok.Data;

/**
 * 封装应用授权请求的类 第三方授权token
 **/
@Data
public class ThirdAuthToken {
	private String accessToken;
	private int expire;
	private String refreshToken;
	private String uid;
	private String openId;
	private String accessCode;
	private String unionId;
}
