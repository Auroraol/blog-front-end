package com.lfj.blog.enums;

/**
 * @Author: LFJ
 * @Date: 2024-01-25 11:03
 * 统一管理响应状态码
 */
public enum ResponseCodeEnum {
	SUCCESS(200000,"响应成功"),
	//后端服务异常以500开头
	SYSTEM_ERROR(500000,"服务异常，请稍后再试"),
	OPERATION_ERROR(500001,"操作失败，请稍后再试"),
	//后端服务异常以400开头
	DATA_PARAM_ERROR(400000,"传入参数错误"),
	ACCOUNT_ALREADY_EXISTS(400001,"账号已存在，请登录"),
	ACCOUNT_NOT_FOUND(400002,"账号不存在"),
	ACCOUNT_LOCK(400003,"账号已锁定，请联系管理员解锁"),
	ACCOUNT_ERROR(400004,"账户密码不匹配"),
	ACCOUNT_REGISTERED_ERROR(400005, "用户名已被注册"),
	ACCOUNT_UPDATETNICKNAME_ERROR(400006, "昵称更新失败"),

	TOKEN_ERROR(401000,"token 已失效,请重新登录"),
	HTTP_NO_PERMISSION(403000,"抱歉，你没有访问权限"),
	HTTP_NO_LOGIN(401002, "登录已失效，请重新登录");

	private final int code;
	private final String message;

	ResponseCodeEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}