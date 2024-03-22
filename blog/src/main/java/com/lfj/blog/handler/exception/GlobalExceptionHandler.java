package com.lfj.blog.handler.exception;

import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.exception.ApiException;
import com.lfj.blog.exception.MobileCodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.lfj.blog.common.response.enums.ResponseCodeEnum.INVALID_REQUEST;

/**
 * @Author: LFJ
 * @Date: 2024-01-25 13:47
 * 统一异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


	/**
	 * 手机号验证不正确 异常
	 *
	 * @return
	 */
	@ExceptionHandler(MobileCodeException.class)
	public ApiResponseResult handleBadMobileCodeException(MobileCodeException e) {
		return ApiResponseResult.fail(INVALID_REQUEST.getCode(), e.getMessage());
	}

	/**
	 * 自定义异常, 如果抛出ApiException的话, 调用
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ApiException.class)
	public ApiResponseResult BizExceptionHandler(ApiException e) {
		log.error("业务异常：{}", e.getMessage());
		return ApiResponseResult.fail(e.getCode(), e.getMessage());
	}

	/**
	 * 其他未知异常, 如果抛出Exception的话及其他未知异常, 调用
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ApiResponseResult exceptionHandler(Exception e) {
		log.error("系统异常：{}", e.getMessage());
		return ApiResponseResult.systemError();
	}
}
