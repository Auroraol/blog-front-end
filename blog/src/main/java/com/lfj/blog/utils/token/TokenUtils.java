package com.lfj.blog.utils.token;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

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

	/**
	 *  解析Token, 判断 jwt 是否过期
	 * @param refreshToken
	 * @return
	 */
	public static Claims parserToken(String refreshToken) {
		try {
			Claims claims = Jwts.parser()
					.verifyWith(SecretKeyUtil.generalKeyByDecoders())
					.build()
					.parseSignedClaims(refreshToken)
					.getPayload();
			return claims;
		} catch (ExpiredJwtException e) {
			// 如果捕获到 ExpiredJwtException 异常，则 JWT 已经过期
			return null;
		} catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
			// 其他异常可能表示 JWT 格式不正确或签名验证失败
			// 根据你的应用需求，你可以选择在这里返回 true 或 false，或者抛出一个自定义异常
			return null; // 或者抛出一个自定义异常
		}
	}
}
