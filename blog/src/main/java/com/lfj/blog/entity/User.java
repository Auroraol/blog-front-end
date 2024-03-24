package com.lfj.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户表
 *
 * @TableName user
 */
@TableName(value = "user")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "User对象", description = "用户表")
public class User implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	/**
	 * 用户id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value = "用户id")
	private Integer id;
	/**
	 * 用户名
	 */
	@TableField(value = "username")
	@ApiModelProperty(value = "用户名")
	private String username;
	/**
	 * 密码
	 */
	@TableField(value = "password")
	@ApiModelProperty(value = "密码")
	private String password;
	/**
	 * 手机号
	 */
	@TableField(value = "mobile")
	@ApiModelProperty(value = "手机号")
	private Long mobile;
	/**
	 * 昵称
	 */
	@TableField(value = "nickname")
	private String nickname;
	/**
	 * 性别，1：男，0：女，默认为1
	 */
	@TableField(value = "gender")
	private Integer gender;
	/**
	 * 生日
	 */
	@TableField(value = "birthday")
	private LocalDate birthday;
	/**
	 * 电子邮箱
	 */
	@TableField(value = "email")
	private String email;
	/**
	 * 简介|个性签名
	 */
	@TableField(value = "brief")
	private String brief;
	/**
	 * 用户头像
	 */
	@TableField(value = "avatar")
	private String avatar;
	/**
	 * 状态，0：正常，1：锁定，2：禁用，3：过期
	 */
	@TableField(value = "status")
	private Integer status;
	/**
	 * 是否管理员，1：是，0：否
	 */
	@TableField(value = "admin")
	private Integer admin;
	/**
	 * 创建时间
	 */
	@TableField(value = "create_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime createTime;
}