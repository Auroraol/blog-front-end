package cn.poile.blog.common.response;


import cn.poile.blog.common.response.enums.ResponseCodeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: LFJ
 * @Date: 2024-01-25 11:02
 * 通用的响应实体类
 */
@Data
@NoArgsConstructor
public class ResponseResult<T>{
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
	public static <T> ResponseResult<T> success(T data) {
		return success(data, ResponseCodeEnum.SUCCESS.getMessage());
	}

	/**
	 * 响应成功的方法
	 *
	 * @param
	 * @return 响应实体 {code: xxx, message: xxx}
	 */
	public static <T> ResponseResult<T> success() {
		ResponseResult<T> result = new ResponseResult<>();
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
	public static <T> ResponseResult<T> success(T data, String message) {
		ResponseResult<T> result = new ResponseResult<>();
		result.setData(data);
		result.setCode(ResponseCodeEnum.SUCCESS.getCode());
		result.setMessage(message);
		return result;
	}


	/**
	 * 响应失败的方法
	 * @param code
	 * @param message
	 * @param <T>
	 * @return
	 */
	public static <T> ResponseResult<T> fail(int code, String message) {
		ResponseResult<T> result = new ResponseResult<>();
		result.setCode(code);
		result.setMessage(message);
		return result;
	}

	private static <T> ResponseResult<T> fail(ResponseCodeEnum responseCode) {
		ResponseResult<T> result = new ResponseResult<>();
		result.setCode(responseCode.getCode());
		result.setMessage(responseCode.getMessage());
		return result;
	}

	/**
	 * 服务异常
	 * @return
	 * @param <T>
	 */
	public static <T> ResponseResult<T> systemError() {
		return fail(ResponseCodeEnum.SYSTEM_ERROR);
	}

	/**
	 * 操作失败
	 * @return
	 * @param <T>
	 */
	public static <T> ResponseResult<T> operationError() {
		return fail(ResponseCodeEnum.OPERATION_ERROR);
	}

	/**
	 * 传入参数错误
	 * @return
	 * @param <T>
	 */
	public static <T> ResponseResult<T> dataParamError() {
		return fail(ResponseCodeEnum.DATA_PARAM_ERROR);
	}

	/**
	 * 账号已存在
	 * @return
	 * @param <T>
	 */
	public static <T> ResponseResult<T> accountAlreadyExists() {
		return fail(ResponseCodeEnum.ACCOUNT_ALREADY_EXISTS);
	}

	/**
	 * 账号不存在
	 * @return
	 * @param <T>
	 */
	public static <T> ResponseResult<T> accountNotFoundError() {
		return fail(ResponseCodeEnum.ACCOUNT_NOT_FOUND);
	}

	/**
	 * 账号己锁定
	 * @return
	 * @param <T>
	 */
	public static <T> ResponseResult<T> accountLock() {
		return fail(ResponseCodeEnum.ACCOUNT_LOCK);
	}

	/**
	 * 账户密码不匹配
	 * @return
	 * @param <T>
	 */
	public static <T> ResponseResult<T> accountError( ) {
		return fail(ResponseCodeEnum.ACCOUNT_ERROR);
	}

	/**
	 * 用户名已被注册
	 * @return
	 * @param <T>
	 */
	public static <T> ResponseResult<T> accountRegisteredError( ) {
		return fail(ResponseCodeEnum.ACCOUNT_REGISTERED_ERROR);
	}

	/**
	 * 	用户名已被注册
	 * @return
	 * @param <T>
	 */
	public static <T> ResponseResult<T> account_updatetnickname_error( ) {
		return fail(ResponseCodeEnum.ACCOUNT_UPDATETNICKNAME_ERROR);
	}

	/**
	 * token已失效,请重新登录
	 * @return
	 * @param <T>
	 */
	public static <T> ResponseResult<T> tokenError( ) {
		return fail(ResponseCodeEnum.TOKEN_ERROR);
	}

	/**
	 * 没权限
	 * @return
	 * @param <T>
	 */
	public static <T> ResponseResult<T> noPermission(){

		return fail(ResponseCodeEnum.HTTP_NO_PERMISSION);
	}

	/**
	 * 没登录
	 * @return
	 * @param <T>
	 */
	public static <T> ResponseResult<T> noLogin() {
		return  fail(ResponseCodeEnum.HTTP_NO_LOGIN);
	}
}