package com.lfj.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 客户端表
 *
 * @TableName client
 */
@TableName(value = "client")
@Data
//@Accessors(chain = true)  //链式, enity没必要, vo可以用
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL) // 为空字段不进行序列化
@Schema(title = "Client对象", description = "客户端表")
public class Client implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	@Schema(description = "id")
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * 客户端id，客户端唯一标识
	 */
	@NotBlank(message = "客户端ID不能为空")
	@TableField(value = "client_id")
	@Schema(description = "客户端id，客户端唯一标识")
	private String clientId;
	/**
	 * 客户端密码
	 */
	@NotBlank(message = "客户端秘钥不能为空")
	@TableField(value = "client_secret")
	@Schema(description = "客户端秘钥")
	private String clientSecret;
	/**
	 * access_token有效时长
	 */
	@TableField(value = "access_token_expire")
	@Schema(description = "access_token有效时长")
	private Long accessTokenExpire;
	/**
	 * refresh_token_expire有效时长
	 */
	@TableField(value = "refresh_token_expire")
	@Schema(description = "refresh_token有效时长")
	private Long refreshTokenExpire;
	/**
	 * 是否支持刷新refresh_token,1:是，0:否
	 */
	@TableField(value = "enable_refresh_token")
	@Schema(description = "是否启用refresh_token,1:是，0:否")
	private Integer enableRefreshToken;
}