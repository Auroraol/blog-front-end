package com.lfj.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName user
 */
@TableName(value = "user")
@Data
public class User implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	/**
	 *
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 *
	 */
	@TableField(value = "phone")
	private String phone;
	/**
	 *
	 */
	@TableField(value = "username")
	private String username;
	/**
	 *
	 */
	@TableField(value = "password")
	private String password;
	/**
	 *
	 */
	@TableField(value = "gender")
	private String gender;
	/**
	 *
	 */
	@TableField(value = "nickname")
	private String nickname;
	/**
	 *
	 */
	@TableField(value = "birthday")
	private String birthday;
	/**
	 *
	 */
	@TableField(value = "email")
	private String email;
	/**
	 *
	 */
	@TableField(value = "personalBrief")
	private String personalbrief;
	/**
	 *
	 */
	@TableField(value = "avatarImgUrl")
	private String avatarimgurl;
	/**
	 *
	 */
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(value = "recentlyLanded")
	private LocalDateTime recentlylanded;
}