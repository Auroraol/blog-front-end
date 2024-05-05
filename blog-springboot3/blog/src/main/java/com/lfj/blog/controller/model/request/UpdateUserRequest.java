package com.lfj.blog.controller.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 用户更新json
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "用户更新json", description = "用户更新")
public class UpdateUserRequest {

	@Schema(description = "用户id")
	@NotNull(message = "用户id不能为空")
	private Integer userId;

	@Schema(description = "昵称")
	private String nickname;

	@Schema(description = "性别，1：男，0：女，默认为1")
	private Integer gender;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@Schema(description = "生日")
	private LocalDate birthday;

	@Schema(description = "简介|个性签名")
	private String brief;
}
