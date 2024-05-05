package com.lfj.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章回复表
 *
 * @TableName article_reply
 */
@TableName(value = "article_reply")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "ArticleReply对象", description = "文章回复表")
public class ArticleReply implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	@Schema(description = "id")
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * 文章id
	 */
	@Schema(description = "文章id")
	@TableField(value = "article_id")
	private Integer articleId;
	/**
	 * 评论id
	 */
	@Schema(description = "评论id")
	@TableField(value = "comment_id")
	private Integer commentId;
	/**
	 * 评论者id
	 */
	@Schema(description = "评论者id")
	@TableField(value = "from_user_id")
	private Integer fromUserId;
	/**
	 * 被评论者id
	 */
	@Schema(description = "被评论者id")
	@TableField(value = "to_user_id")
	private Integer toUserId;
	/**
	 * 回复内容
	 */
	@Schema(description = "回复内容")
	@TableField(value = "content")
	private String content;

	/**
	 * 回复时间
	 */
	@Schema(description = "回复时间")
	@JsonSerialize(using = LocalDateTimeSerializer.class)  //用于redis
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)//用于redis
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@TableField(value = "reply_time")
	private LocalDateTime replyTime;

	/**
	 * 是否已删除，1:是，0:否
	 */
	@TableLogic
	@JsonIgnore   // 忽略
	@Schema(description = "是否已删除，1:是，0:否")
	@TableField(value = "deleted")
	private Integer deleted;
}