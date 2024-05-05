package com.lfj.blog.controller.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 登录返回前端AccessToken
 **/
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "AccessTokenDTO", description = "AccessTokenDTO")
public class AccessTokenDTO {

	@JsonProperty("access_token")
	@Schema(description = "access_token")
	private String accessToken;

	@JsonProperty("token_type")
	@Schema(description = "token类型:Bearer")
	private String tokenType;

	@JsonProperty("refresh_token")
	@Schema(description = "refresh_token")
	private String refreshToken;
}
