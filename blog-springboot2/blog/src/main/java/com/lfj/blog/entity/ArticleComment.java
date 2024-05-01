package com.lfj.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@EqualsAndHashCode(callSuper = false)  //默认false, 这里其实可以不写
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "ArticleComment对象", description = "文章评论表")
public class ArticleComment implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * 文章id
	 */
	@ApiModelProperty(value = "文章id")
	@TableField(value = "article_id")
	private Integer articleId;
	/**
	 * 评论者id
	 */
	@ApiModelProperty(value = "评论者id")
	@TableField(value = "from_user_id")
	private Integer fromUserId;
	/**
	 * 评论内容
	 */
	@ApiModelProperty(value = "评论内容")
	@TableField(value = "content")
	private String content;
	/**
	 * 评论时间
	 */
	@ApiModelProperty(value = "评论时间")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@TableField(value = "comment_time")
	private LocalDateTime commentTime;
	/**
	 * 是否已删除，1：是，0：否
	 */
	@JsonIgnore
	@ApiModelProperty(value = "是否已删除,1:是，0:否")
	@TableField(value = "deleted")
	private Integer deleted;
}