package com.lfj.blog.common.response.enums;

/**
 * @Author: LFJ
 * @Date: 2024-01-25 11:03
 * 统一管理响应状态码
 */
public enum ResponseCodeEnum {
	/**
	 * 成功
	 */
	SUCCESS(200000, "响应成功"),

	//后端服务异常以500开头
	/**
	 * 服务异常
	 */
	SYSTEM_ERROR(500000, "服务异常"),
	/**
	 * 操作失败，请稍后再试
	 */
	OPERATION_ERROR(500001, "操作失败"),

	//后端服务异常以400开头
	/**
	 * 传入参数错误
	 */
	DATA_PARAM_ERROR(400000, "传入参数错误"),

	/**
	 * 用户名或密码错误
	 */
	ACCOUNT_ERROR(400001, "用户名或密码错误"),

	/**
	 * 账号不存在
	 */
	ACCOUNT_NOT_FOUND(400002, "账号不存在"),

	/**
	 * 账号已锁定
	 */
	ACCOUNT_LOCK(400003, "账号已锁定，请联系管理员解锁"),

	/**
	 * 账号禁用
	 */
	ACCOUNT_DISABLE(400004, "账号已禁用"),

	/**
	 * 账号已过期
	 */
	ACCOUNT_EXPIRED(400005, "账号已过期"),

	/**
	 * 账号已锁定
	 */
	ACCOUNT_LOCKED(400006, "账号已锁定"),

	/**
	 * 凭证已过期
	 */
	CREDENTIALS_EXPIRED(400007, "凭证已过期"),

	/**
	 * 不允许访问
	 */
	ACCESS_DENIED(400008, "授权失败, 不允许访问"),

	/**
	 * 无权限访问
	 */
	PERMISSION_DENIED(400009, "认证失败, 无权限访问, 请重新登录"),

	/**
	 * 凭证无效或已过期
	 */
	CREDENTIALS_INVALID(4000010, "凭证无效或已过期"),

	/**
	 * 刷新凭证无效或已过期
	 */
	REFRESH_CREDENTIALS_INVALID(4000011, "刷新凭证无效或已过期"),

	/**
	 * 无效请求
	 */
	INVALID_REQUEST(4000012, "无效请求或请求不接受"),

	/**
	 * 接口限流
	 */
	REQUEST_LIMIT(4000013, "接口访问次数限制"),

	/**
	 * 用户名已被注册
	 */
	ACCOUNT_REGISTERED_ERROR(4000014, "用户名已被注册"),

	/**
	 * 昵称更新失败
	 */
	ACCOUNT_UPDATETNICKNAME_ERROR(4000015, "昵称更新失败");

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