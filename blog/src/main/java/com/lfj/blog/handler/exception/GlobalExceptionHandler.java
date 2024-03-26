package com.lfj.blog.handler.exception;

import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.exception.ApiException;
import com.lfj.blog.exception.MobileCodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
	 * RequestParam 参数格式校验不通过 异常(Validator校验框架臃肿所以单独拦截参数校验的异常)
	 *
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ApiResponseResult handleConstraintViolationException(ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
		List<ConstraintViolation<?>> list = new ArrayList<>(constraintViolations);
		ConstraintViolation<?> constraintViolation = list.get(0);
		return ApiResponseResult.fail(INVALID_REQUEST.getCode(), constraintViolation.getMessage());
	}

	/**
	 * RequestBody 参数校验不通过 异常(Validator校验框架臃肿所以单独拦截参数校验的异常)
	 *
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ApiResponseResult handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		return ApiResponseResult.fail(INVALID_REQUEST.getCode(), fieldErrors.get(0).getDefaultMessage());
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
	 * 处理AccessDeineHandler无权限异常
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public void accessDeniedException(AccessDeniedException e) throws AccessDeniedException {
		// 将 Spring Security 异常继续抛出，以便交给自定义处理器处理
		throw e;
	}

	/**
	 * 其他未知异常, 如果抛出Exception的话及其他未知异常, 调用
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ApiResponseResult exceptionHandler(Exception e) throws Exception {
		log.error("系统异常：{}", e.getMessage());
		return ApiResponseResult.systemError();
	}
}
