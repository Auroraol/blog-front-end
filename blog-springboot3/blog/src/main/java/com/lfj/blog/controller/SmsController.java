package com.lfj.blog.controller;

import com.lfj.blog.common.constant.RedisPrefixConstant;
import com.lfj.blog.common.limiter.annotation.RateLimiter;
import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.common.sms.service.SmsCodeService;
import com.lfj.blog.common.validator.annotation.IsPhone;
import com.lfj.blog.service.security.biz.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信服务
 *
 * @Author: LFJ
 * @Date: 2024-01-25 14:02
 **/
@Log4j2
@RestController
@RequestMapping("/sms")
@Tag(name = "短信验证码服务", description = "/sms")
public class SmsController {

	@Autowired
	private SmsCodeService smsCodeService;

	@Autowired
	private EmailService emailService;

	@PostMapping("/send")
	@RateLimiter(name = RedisPrefixConstant.SMS_LIMIT_NAME, max = 1, key = "#mobile", timeout = 120L, extra = "smsLimiter")
	@Operation(summary = "发送短信验证码", description = "验证码有效时5分钟;同一手机号每天只能发10次;同一ip每天只能发10次;同一手机号限流120s一次")
	public ApiResponseResult sendSmsCode(@Parameter(description = "手机号") @NotNull @IsPhone @RequestParam String mobile) {
		smsCodeService.sendSmsCode(mobile);
		return ApiResponseResult.success();
	}
}
