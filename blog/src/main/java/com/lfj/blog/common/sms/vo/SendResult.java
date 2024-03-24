package com.lfj.blog.common.sms.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 短信发送结果
 **/
@Data
@AllArgsConstructor
public class SendResult {

	/**
	 * 是否发送成功
	 */
	private boolean success;

	/**
	 * 发送的验证码
	 */
	private String code;
}