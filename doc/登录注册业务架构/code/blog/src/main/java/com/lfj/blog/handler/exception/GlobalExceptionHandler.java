package com.lfj.blog.handler.exception;

import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: LFJ
 * @Date: 2024-01-25 13:47
 * 统一异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	//如果抛出BizException的话, 调用
	@ExceptionHandler(BizException.class)
	public ResponseResult BizExceptionHandler(BizException e) {
		log.error("业务异常：{}", e.getMessage());
		return ResponseResult.fail(e.getCode(), e.getMessage());
	}
//
	//如果抛出Exception的话, 调用
	@ExceptionHandler(Exception.class)
	public ResponseResult exceptionHandler(Exception e) {
		log.error("系统异常：{}", e.getMessage());
		return ResponseResult.systemError();
	}
}
