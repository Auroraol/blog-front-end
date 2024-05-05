package com.lfj.blog.controller.model.request;

import com.lfj.blog.common.validator.annotation.IsPhone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

/**
 * 用户注册请json
 *
 * @Author: LFJ
 * @Date: 2024-04-3 15:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(title = "用户注册请json", description = "用户注册")
public class UserRegisterRequest {

	@NotBlank(message = "用户名不能为空")
	@Schema(description = "用户名只能字母开头，允许2-16字节，允许字母数字下划线")
	@Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{1,15}$", message = "用户名只能字母开头，允许2-16字节，允许字母数字下划线")
	private String username;

	@NotBlank(message = "密码不能为空")
	@Length(min = 6, message = "密码至少6位数")
	@Schema(description = "密码")
	private String password;

	@NotNull(message = "手机号不能为空")
	@IsPhone
	@Schema(description = "手机号")
	private String mobile;

	@NotBlank(message = "验证码不能为空")
	@Schema(description = "验证码")
	private String code;
}
