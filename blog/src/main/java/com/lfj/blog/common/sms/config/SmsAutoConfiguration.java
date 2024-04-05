package com.lfj.blog.common.sms.config;

import com.lfj.blog.common.sms.service.SmsCodeService;
import com.lfj.blog.common.sms.service.impl.ali.AliSmsCodeServiceImpl;
import com.lfj.blog.common.sms.service.impl.tencent.TencentSmsCodeServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 短信验证码服务自动配置，默认阿里云短信服务
 **/
@Log4j2
@Configuration
@EnableConfigurationProperties({SmsServiceProperties.class})
public class SmsAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean  // 确保在容器中只会有一个名为 "smsService" 的 Bean，
	public SmsCodeService smsService(SmsServiceProperties properties) {
		int type = properties.getType();
		if (type == 1) {
			return new AliSmsCodeServiceImpl(properties);
		}
		return new TencentSmsCodeServiceImpl(properties);
	}
}
