package com.lfj.blog.common.sms.service.impl.tencent;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import com.lfj.blog.common.sms.config.SmsServiceProperties;
import com.lfj.blog.common.sms.service.impl.BaseSmsCodeServiceImpl;
import com.lfj.blog.common.sms.vo.SendResult;
import com.lfj.blog.exception.ApiException;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;

/**
 * 腾讯短信服务
 * <p>
 * 2020-05-18 10:38
 **/
@Log4j2
public class TencentSmsCodeServiceImpl extends BaseSmsCodeServiceImpl {

	private String appId;
	private String appKey;
	private String templateId;
	private String signName;

	public TencentSmsCodeServiceImpl(SmsServiceProperties properties) {

	}

	/**
	 * 发送短信验证码实现
	 *
	 * @param mobile 手机号
	 * @return
	 */
	@Override
	protected SendResult handleSendSmsCode(String mobile) {
		SmsSingleSender sender = new SmsSingleSender(Integer.parseInt(appId), appKey);
		ArrayList<String> params = new ArrayList<>();
		String code = createCode();
		params.add(code);
		// 默认只能发送中国大陆的短信86
		try {
			SmsSingleSenderResult result = sender.sendWithParam("86", mobile, Integer.parseInt(templateId), params, signName, "", "");
			if (result.result != 0) {
				throw new ApiException(ResponseCodeEnum.SYSTEM_ERROR.getCode(), result.errMsg);
			}
			return new SendResult(true, code);
		} catch (Exception e) {
			log.error("发送短信失败:{0}", e);
			throw new ApiException(ResponseCodeEnum.SYSTEM_ERROR.getCode(), "短信发送失败");
		}
	}
}
