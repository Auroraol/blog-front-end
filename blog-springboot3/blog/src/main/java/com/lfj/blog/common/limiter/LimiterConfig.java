package com.lfj.blog.common.limiter;

import com.lfj.blog.common.sms.config.SmsServiceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 注册ExtraLimiter bean
 */
@Component
public class LimiterConfig {

    @Bean("smsLimiter")
    public ExtraLimiter limiter(SmsServiceProperties properties) {
        SmsLimiter smsLimiter = new SmsLimiter();
        smsLimiter.setDayMax(properties.getDayMax());
        return smsLimiter;
    }
}
