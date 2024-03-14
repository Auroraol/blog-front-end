package com.lfj.blog.utils.token;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Jwts;

import java.util.Date;

/**
 * @Author: LFJ
 * @Date: 2024-03-09 15:21
 */
public class TokenUtils {
	public static String createToken(String username, Object claim, Long expirationTime) {
		//JWT 生成
		return Jwts.builder()
				//jwt 私有声明
				.claim(SecurityKey.USER_CONTEXT, JSON.toJSONString(claim))
				//JWT的主体
				.subject(username)
				//失效时间 当前时间+过期分钟
				.expiration(new Date(System.currentTimeMillis() + expirationTime))
				//签名算法和密钥
				.signWith(SecretKeyUtil.generalKey())
				.compact();
	}
}
