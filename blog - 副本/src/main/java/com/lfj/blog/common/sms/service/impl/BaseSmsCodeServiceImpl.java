package com.lfj.blog.common.sms.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lfj.blog.common.constant.RedisPrefixConstant;
import com.lfj.blog.common.sms.service.SmsCodeService;
import com.lfj.blog.common.sms.vo.SendResult;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

@Data
public abstract class BaseSmsCodeServiceImpl implements SmsCodeService,
		InitializingBean, ApplicationContextAware {

	private ApplicationContext applicationContext; //Spring的ApplicationContext的持有者,可以获取spring容器中的bean
	private StringRedisTemplate redisTemplate;
	/**
	 * 短信验证码有效时间
	 */
	private long expire = 300L;


	// 和@Autowired作用一样
	//从应用程序上下文中获取 StringRedisTemplate 类型的 Bean，并进行必要的校验，确保该 Bean 的正确性和可用性
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (this.applicationContext == null) {
			this.applicationContext = applicationContext;
		}
	}

	@Override
	public void afterPropertiesSet() {
		if (this.redisTemplate == null) {
			this.redisTemplate = applicationContext.getBean(StringRedisTemplate.class);
		}
		Assert.notNull(this.redisTemplate, "There is no one available StringRedisTemplate bean");
	}

	/**
	 * 发送短信验证码，这个提供外部调用的
	 * 发送短信成功后缓存短信验证码
	 *
	 * @param mobile
	 * @return
	 */
	@Override
	public boolean sendSmsCode(String mobile) {
		SendResult sendResult = handleSendSmsCode(mobile);
		String code = sendResult.getCode();
		boolean smsSuccess = sendResult.isSuccess();
//		System.out.println(code);
//		System.out.println(smsSuccess);
		if (!StringUtils.isBlank(code) && smsSuccess) {
			cacheSmsCode(mobile, code);
			return true;
		}
		return false;
	}

	/**
	 * 发送短信验证码实现, 抽象方法具体实现由子类实现
	 *
	 * @param mobile 手机号
	 * @return
	 */
	protected abstract SendResult handleSendSmsCode(String mobile);

	/**
	 * 缓存短信验证码
	 *
	 * @param mobile
	 * @param code
	 */
	@Override
	public void cacheSmsCode(String mobile, String code) {
		redisTemplate.opsForValue().set(RedisPrefixConstant.SMS_CODE + mobile, code,
				expire, TimeUnit.SECONDS);
	}

	/**
	 * 校验短信验证码
	 *
	 * @param mobile
	 * @param code
	 * @return
	 */
	@Override
	public boolean checkSmsCode(String mobile, String code) {
		String cacheCode = redisTemplate.opsForValue().get(RedisPrefixConstant.SMS_CODE + mobile);
		return !StringUtils.isBlank(cacheCode) && cacheCode.equals(code);
	}

	/**
	 * 删除缓存短信验证码
	 *
	 * @param mobile
	 * @return
	 */
	@Override
	public boolean deleteSmsCode(String mobile) {
		redisTemplate.delete(RedisPrefixConstant.SMS_CODE + mobile);
		return true;
	}

	/**
	 * 获取随机6位数验证码
	 *
	 * @return
	 */
	protected String createCode() {
		int random = (int) ((Math.random() * 9 + 1) * 100000);
		return String.valueOf(random);
	}


}
