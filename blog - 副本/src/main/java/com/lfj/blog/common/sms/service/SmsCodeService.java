package com.lfj.blog.common.sms.service;

/**
 * 短信服务
 */
public interface SmsCodeService {
	/**
	 * 发送短信验证码
	 *
	 * @param mobile 手机号
	 * @return
	 */
	boolean sendSmsCode(String mobile);

	/**
	 * 缓存短信验证码
	 *
	 * @param mobile
	 * @param code
	 */
	void cacheSmsCode(String mobile, String code);

	/**
	 * 校验短信验证码
	 *
	 * @param mobile
	 * @param code
	 * @return
	 */
	boolean checkSmsCode(String mobile, String code);

	/**
	 * 删除验证码
	 *
	 * @param mobile
	 * @return
	 */
	boolean deleteSmsCode(String mobile);
}
