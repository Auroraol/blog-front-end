package com.lfj.blog.exception;

import com.lfj.blog.common.response.enums.ResponseCodeEnum;

/**
 * @Author: LFJ
 * @Date: 2024-01-25 14:02
 * 自定义的异常类-业务中发生的异常
 */
public class ApiException extends RuntimeException {
	private int code;
	private String errorMsg;

	public ApiException(ResponseCodeEnum responseCode) {
		super(responseCode.getMessage());
		this.code = responseCode.getCode();
	}

	public ApiException(int errorCode, String errorMsg) {
		super(errorMsg);
		this.code = errorCode;
		this.errorMsg = errorMsg;
	}
//	public BizException(ResponseResult responseResult) {
//		super(responseResult.getMessage());
//		this.code = responseResult.getCode();
//	}
	// Getter 方法
	public int getCode() {
		return code;
	}
}
