package com.lfj.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章评论表
 *
 * @TableName article_comment
 */
@TableName(value = "article_comment")
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleComment implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * 文章id
	 */
	@TableField(value = "article_id")
	private Integer articleId;
	/**
	 * 评论者id
	 */
	@TableField(value = "from_user_id")
	private Integer fromUserId;
	/**
	 * 评论内容
	 */
	@TableField(value = "content")
	private String content;
	/**
	 * 评论时间
	 */
	@TableField(value = "comment_time")
	private LocalDateTime commentTime;
	/**
	 * 是否已删除，1：是，0：否
	 */
	@TableField(value = "deleted")
	private Integer deleted;
}