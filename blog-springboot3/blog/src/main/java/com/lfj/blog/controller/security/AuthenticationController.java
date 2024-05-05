package com.lfj.blog.controller.security;


import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import com.lfj.blog.common.security.token.AuthenticationToken;
import com.lfj.blog.common.security.token.RedisTokenStore;
import com.lfj.blog.common.sms.service.SmsCodeService;
import com.lfj.blog.common.validator.annotation.IsPhone;
import com.lfj.blog.controller.model.dto.AccessTokenDTO;
import com.lfj.blog.entity.Client;
import com.lfj.blog.exception.ApiException;
import com.lfj.blog.service.IClientService;
import com.lfj.blog.service.security.AuthenticationService;
import com.lfj.blog.service.security.biz.OauthService;
import com.lfj.blog.utils.BeanCopyUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: LFJ
 * @Date: 2024-03-03 16:15
 **/
@RestController
@Log4j2
@Tag(name = "认证服务", description = "/")
public class AuthenticationController {

	private static final String TOKEN_TYPE = "Bearer";

	private static final String AUTHORIZATION_TYPE = "Basic";

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RedisTokenStore tokenStore;

	@Autowired
	private SmsCodeService smsCodeService;

	@Autowired
	private IClientService clientService;


	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private OauthService oauthService;

	@PostMapping("/account/login")
	@Operation(summary = "账号密码登录", description = "账号可以是用户名或手机号")
	public ApiResponseResult<AccessTokenDTO> accountLogin(@Parameter(description = "用户名或手机号") @NotBlank(message = "账号不能为空") @RequestParam String username,
														  @Parameter(description = "密码") @NotBlank(message = "密码不能为空") @RequestParam String password,
														  @Parameter(description = "客户端认证请求头") @RequestHeader(value = "Authorization") String authorization) {
		// 通过客户端认证请求头查询Client
		Client client = getAndValidatedClient(authorization);
		try {
			//生成响应的token
			AuthenticationToken authenticationToken = authenticationService.usernameOrMobilePasswordAuthenticate(username, password, client);
			// 部分返回
			AccessTokenDTO accessTokenDTO = BeanCopyUtil.copyObject(authenticationToken, AccessTokenDTO.class);
			return ApiResponseResult.success(accessTokenDTO);
		} catch (BadCredentialsException e) {
			return ApiResponseResult.accountError();
		}
	}

	@PostMapping("/mobile/login")
	@Operation(summary = "手机号验证码登录", description = "验证码调用发送验证码接口获取")
	public ApiResponseResult<AccessTokenDTO> mobileLogin(@Parameter(description = "手机号") @NotNull(message = "手机号不能为空") @IsPhone @RequestParam String mobile,
														 @Parameter(description = "手机号验证码") @NotBlank(message = "验证码不能为空") @RequestParam String code,
														 @Parameter(description = "客户端认证请求头") @RequestHeader(value = "Authorization") String authorization) {
		Client client = getAndValidatedClient(authorization);
		//生成响应的token
		AuthenticationToken authenticationToken = authenticationService.mobileCodeAuthenticate(mobile, code, client);
		AccessTokenDTO accessTokenDTO = BeanCopyUtil.copyObject(authenticationToken, AccessTokenDTO.class);
		return ApiResponseResult.success(accessTokenDTO);
	}

	@GetMapping("/oauth")
	@Operation(summary = "第三方登录", description = "不需要accessToken")
	public ApiResponseResult<AccessTokenDTO> oauth(
			@Parameter(description = "认证类型") @NotNull(message = "认证类型不能为空") @RequestParam Integer state,
			@Parameter(description = "第三方授权码") @NotBlank(message = "授权码不能为空") @RequestParam String code,
			@Parameter(description = "客户端认证请求头") @RequestHeader(value = "Authorization") String authorization) {
		Client client = getAndValidatedClient(authorization);
		// 第三方 //生成响应的token
		AuthenticationToken authenticationToken = oauthService.oauth(state, code, client);
		AccessTokenDTO accessTokenDTO = BeanCopyUtil.copyObject(authenticationToken, AccessTokenDTO.class);
		return ApiResponseResult.success(accessTokenDTO);
	}

	@DeleteMapping("/logout")
	@Operation(summary = "用户登出")
	public ApiResponseResult logout(@RequestHeader(value = "Authorization") String authorization,
									@Parameter(description = "access_token") @RequestParam("access_token") String accessToken) {
		Client client = getAndValidatedClient(authorization);
		authenticationService.remove(accessToken, client);
		return ApiResponseResult.success();
	}

	@PostMapping("/refresh_access_token")
	@Operation(summary = "刷新accessToken")
	public ApiResponseResult<AccessTokenDTO> refreshAccessToken(
			@Parameter(description = "客户端认证请求头") @RequestHeader(value = "Authorization") String authorization,
			@Parameter(description = "refresh_token") @NotBlank(message = "refresh_token不能为空") @RequestParam("refresh_token") String refreshToken) {
		Client client = getAndValidatedClient(authorization);
		AuthenticationToken authenticationToken = authenticationService.refreshAccessToken(refreshToken, client);
		AccessTokenDTO response = new AccessTokenDTO();
		BeanUtils.copyProperties(authenticationToken, response);
		return ApiResponseResult.success(response);
	}

	/**
	 * 获取并校验client
	 *
	 * @param authorization
	 * @return
	 */
	private Client getAndValidatedClient(String authorization) {
		String[] clientIdAndClientSecret = extractClientIdAndClientSecret(authorization); // 符合基本格式
		String clientId = clientIdAndClientSecret[0];
		String clientSecret = clientIdAndClientSecret[1];
		//通过clientService 去数据库查找
		Client client = clientService.getClientByClientId(clientId);
		if (client == null || !clientSecret.equals(client.getClientSecret())) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "无效客户端");
		}
		return client;
	}

	/**
	 * 提取客户端id和客户端密码, 是否符号格式 Basic cGM6MTIzNDU2
	 * 其中cGM6MTIzNDU2为pc:123456 经过base64加密后的密文。
	 * pc为clientId，123456为clientSecret。
	 *
	 * @param authorization
	 * @return
	 */
	private String[] extractClientIdAndClientSecret(String authorization) {
		if (!authorization.startsWith(AUTHORIZATION_TYPE)) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "无效客户端");
		}
		String base64Data = authorization.substring(6); // 以索引6开始, 如Basic cGM6MTIzNDU2 -> cGM6MTIzNDU2
		try {
			String data = new String(Base64.decodeBase64(base64Data)); //解密
			log.info(data);
			String separator = ":";
			String[] split = data.split(separator);
			int length = split.length;
			int matched = 2;
			if (length != matched) {
				throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "无效客户端");
			}
			return split;
		} catch (Exception e) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "无效客户端");
		}
	}
}
