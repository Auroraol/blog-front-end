package com.lfj.blog.common.constant;

/**
 * Redis缓存前缀
 **/
public class RedisPrefixConstant {

	/**
	 * 短信验证码 key前缀
	 */
	public static final String SMS_CODE = "sms:code:";

	/**
	 * 文章浏览 key前缀
	 */
	public static final String ART_VIEW = "art:view:";


	/**
	 * 限流前缀
	 */
	public static final String REDIS_LIMIT_KEY_PREFIX = "limit:";

	/**
	 * 短信限流key前缀（命名空间）
	 */
	public static final String SMS_LIMIT_NAME = "sms";

}
