package com.lfj.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 友链表
 *
 * @TableName friend_link
 */
@TableName(value = "friend_link")
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "FriendChain对象", description = "友链表")
public class FriendLink implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	@Schema(description = "id")
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * 名称
	 */
	@Schema(description = "名称")
	@TableField(value = "name")
	private String name;
	/**
	 * 链接
	 */
	@Schema(description = "链接")
	@TableField(value = "url")
	private String url;
	/**
	 * 图标
	 */
	@Schema(description = "图标")
	@TableField(value = "icon")
	private String icon;
}