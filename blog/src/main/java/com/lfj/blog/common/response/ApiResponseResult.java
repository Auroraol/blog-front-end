package com.lfj.blog.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: LFJ
 * @Date: 2024-01-25 11:02
 * 通用的响应实体类
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // 为空字段不进行序列化
public class ApiResponseResult<T> {
	private int code;
	private T data;
	private String message;

	/**
	 * 响应成功的方法
	 *
	 * @param data 获取的数据
	 * @param <T>  泛型类型
	 * @return 响应实体
	 */
	public static <T> ApiResponseResult<T> success(T data) {
		return success(data, ResponseCodeEnum.SUCCESS.getMessage());
	}

	/**
	 * 响应成功的方法
	 *
	 * @param
	 * @return 响应实体 {code: xxx, message: xxx}
	 */
	public static <T> ApiResponseResult<T> success() {
		ApiResponseResult<T> result = new ApiResponseResult<>();
		result.setCode(ResponseCodeEnum.SUCCESS.getCode());
		result.setMessage(ResponseCodeEnum.SUCCESS.getMessage());
		return result;
	}


	/**
	 * 响应成功的方法，可以自定义提示信息
	 *
	 * @param data    获取的数据
	 * @param message 提示信息
	 * @param <T>     泛型类型
	 * @return 响应实体
	 */
	public static <T> ApiResponseResult<T> success(T data, String message) {
		ApiResponseResult<T> result = new ApiResponseResult<>();
		result.setData(data);
		result.setCode(ResponseCodeEnum.SUCCESS.getCode());
		result.setMessage(message);
		return result;
	}


	/**
	 * 响应失败的方法
	 *
	 * @param code
	 * @param message
	 * @param <T>
	 * @return
	 */
	public static <T> ApiResponseResult<T> fail(int code, String message) {
		ApiResponseResult<T> result = new ApiResponseResult<>();
		result.setCode(code);
		result.setMessage(message);
		return result;
	}

	private static <T> ApiResponseResult<T> fail(ResponseCodeEnum responseCode) {
		ApiResponseResult<T> result = new ApiResponseResult<>();
		result.setCode(responseCode.getCode());
		result.setMessage(responseCode.getMessage());
		return result;
	}

	/**
	 * 服务异常
	 *
	 * @param <T>
	 * @return
	 */
	public static <T> ApiResponseResult<T> systemError() {
		return fail(ResponseCodeEnum.SYSTEM_ERROR);
	}

	/**
	 * 操作失败
	 *
	 * @param <T>
	 * @return
	 */
	public static <T> ApiResponseResult<T> operationError() {
		return fail(ResponseCodeEnum.OPERATION_ERROR);
	}

	/**
	 * 传入参数错误
	 *
	 * @param <T>
	 * @return
	 */
	public static <T> ApiResponseResult<T> dataParamError() {
		return fail(ResponseCodeEnum.DATA_PARAM_ERROR);
	}


	/**
	 * 账号不存在
	 *
	 * @param <T>
	 * @return
	 */
	public static <T> ApiResponseResult<T> accountNotFoundError() {
		return fail(ResponseCodeEnum.ACCOUNT_NOT_FOUND);
	}

	/**
	 * 账号己锁定
	 *
	 * @param <T>
	 * @return
	 */
	public static <T> ApiResponseResult<T> accountLock() {
		return fail(ResponseCodeEnum.ACCOUNT_LOCK);
	}

	/**
	 * 账户密码不匹配
	 *
	 * @param <T>
	 * @return
	 */
	public static <T> ApiResponseResult<T> accountError() {
		return fail(ResponseCodeEnum.ACCOUNT_ERROR);
	}

	/**
	 * 用户名已被注册
	 *
	 * @param <T>
	 * @return
	 */
	public static <T> ApiResponseResult<T> accountRegisteredError() {
		return fail(ResponseCodeEnum.ACCOUNT_REGISTERED_ERROR);
	}

	/**
	 * 昵称更新失败
	 *
	 * @param <T>
	 * @return
	 */
	public static <T> ApiResponseResult<T> account_updatetnickname_error() {
		return fail(ResponseCodeEnum.ACCOUNT_UPDATETNICKNAME_ERROR);
	}

	/**
	 * 凭证已过期
	 *
	 * @param <T>
	 * @return
	 */
	public static <T> ApiResponseResult<T> tokenError() {
		return fail(ResponseCodeEnum.CREDENTIALS_EXPIRED);
	}


	/**
	 * 授权失败, 不允许访问
	 *
	 * @param <T>
	 * @return
	 */
	public static <T> ApiResponseResult<T> access_denied() {

		return fail(ResponseCodeEnum.ACCESS_DENIED);
	}

	/**
	 * 认证失败，无权限访问，请重新登录")
	 *
	 * @param <T>
	 * @return
	 */
	public static <T> ApiResponseResult<T> noPermission() {

		return fail(ResponseCodeEnum.PERMISSION_DENIED);
	}

	/**
	 * 凭证无效或已过期
	 *
	 * @param <T>
	 * @return
	 */
	public static <T> ApiResponseResult<T> credentials_invalid() {

		return fail(ResponseCodeEnum.CREDENTIALS_INVALID);
	}

	/**
	 * 无效请求
	 *
	 * @param <T>
	 * @return
	 */
	public static <T> ApiResponseResult<T> invalidRequest() {

		return fail(ResponseCodeEnum.INVALID_REQUEST);
	}

}