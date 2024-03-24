package com.lfj.blog.common.sms.service.impl.ali;

import cn.hutool.json.JSONUtil;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponseBody;
import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import com.lfj.blog.common.sms.config.SmsServiceProperties;
import com.lfj.blog.common.sms.service.impl.BaseSmsCodeServiceImpl;
import com.lfj.blog.common.sms.vo.SendResult;
import com.lfj.blog.exception.ApiException;
import darabonba.core.client.ClientOverrideConfiguration;
import darabonba.core.exception.ClientException;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 阿里云短信验证码
 */
@Log4j2
public class AliSmsCodeServiceImpl extends BaseSmsCodeServiceImpl {

	private static final String DOMAIN = "dysmsapi.aliyuncs.com"; // 访问的域名，不要修改

	/**
	 * 地域id
	 */
	private String regionId;
	/**
	 * 子用户的访问键
	 */
	private String accessKeyId;
	/**
	 * 子用户的访问密钥
	 */
	private String accessKeySecret;
	/**
	 * 签名名称
	 */
	private String signName;
	/**
	 * 登录短信模板的code
	 */
	private String templateCode;


	// 读取配置信息
	public AliSmsCodeServiceImpl(SmsServiceProperties properties) {
		setExpire(properties.getExpire());
		SmsServiceProperties.Ali ali = properties.getAli();
		init(ali);
	}

	private void init(SmsServiceProperties.Ali ali) {
		this.regionId = ali.getRegionId();
		this.accessKeyId = ali.getAccessKeyId();
		this.accessKeySecret = ali.getAccessKeySecret();
		this.signName = ali.getSignName();
		this.templateCode = ali.getTemplateCode();
	}


	/**
	 * 发送短信验证码
	 *
	 * @param mobile
	 * @return
	 */
	@Override
	protected SendResult handleSendSmsCode(String mobile) {

		// 配置凭据身份验证信息，包括 accessKeyId 与 accessKeySecret
		StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
				.accessKeyId(accessKeyId)
				.accessKeySecret(accessKeySecret)
				.build());

		// 客户端配置
		AsyncClient client = AsyncClient.builder()
				// 地域id
				.region(regionId)
				.credentialsProvider(provider)
				.overrideConfiguration(
						ClientOverrideConfiguration.create()
								// 访问的域名，不要修改
								.setEndpointOverride(DOMAIN)
								// 设置超时时长
								.setConnectTimeout(Duration.ofSeconds(30))
				)
				.build();

		// 请求参数设置
		HashMap<String, String> contentParam = new HashMap<>();
		String code = createCode();  // 设置验证码
		contentParam.put("code", code);
		SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
				.signName(signName)
				.templateCode(templateCode)
				.phoneNumbers(String.valueOf(mobile))
				.templateParam(JSONUtil.toJsonStr(contentParam))  //
				.build();

		CompletableFuture<SendSmsResponse> response = null;

		try {
			// 异步获取API请求的返回值
			response = client.sendSms(sendSmsRequest);
			// 同步获取API请求的返回值
			SendSmsResponseBody body = response.get().getBody();
			// 判断是否发送成功
			if ("OK".equalsIgnoreCase(body.getCode())) {
				// 返回
				return new SendResult(true, code);
			} else {
				throw new ApiException(ResponseCodeEnum.SYSTEM_ERROR.getCode(), "短信发送失败");
			}
		} catch (ClientException e) {
			log.error("发送短信失败:{0}", e);
			throw new ApiException(ResponseCodeEnum.SYSTEM_ERROR.getCode(), "短信发送失败");
		} catch (ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			// 关闭客户端
			client.close();
		}
	}
}
