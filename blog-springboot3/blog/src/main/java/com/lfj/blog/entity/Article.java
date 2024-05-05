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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章表
 *
 * @TableName article
 */
@TableName(value = "article")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "Article对象", description = "文章表")
public class Article implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	/**
	 * 文章id
	 */
	@Schema(description = "文章id")
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * 是否原创，1：是，0：否
	 */
	@Schema(description = "是否原创，1:是，0:否")
	@TableField(value = "original")
	private Integer original;
	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	@TableField(value = "user_id")
	private Integer userId;
	/**
	 * 分类名称-冗余字段
	 */
	@Schema(description = "分类名称-冗余字段")
	@TableField(value = "category_name")
	private String categoryName;
	/**
	 * 文章分类id
	 */
	@Schema(description = "文章分类id")
	@TableField(value = "category_id")
	private Integer categoryId;
	/**
	 * 文章标题
	 */
	@Schema(description = "文章标题")
	@TableField(value = "title")
	private String title;
	/**
	 * 文章摘要
	 */
	@Schema(description = "文章摘要")
	@TableField(value = "summary")
	private String summary;
	/**
	 * 文章内容
	 */
	@Schema(description = "文章内容")
	@TableField(value = "content")
	private String content;
	/**
	 * 文章富文本内容
	 */
	@Schema(description = "文章富文本内容")
	@TableField(value = "html_content")
	private String htmlContent;
	/**
	 * 文章封面
	 */
	@Schema(description = "文章封面")
	@TableField(value = "cover")
	private String cover;
	/**
	 * 文章状态：0为正常，1为待发布，2为回收站
	 */
	@Schema(description = "文章状态：0为正常，1为待发布，2为回收站")
	@TableField(value = "status")
	private Integer status;
	/**
	 * 文章浏览次数
	 */
	@Schema(description = "文章浏览次数")
	@TableField(value = "view_count")
	private Integer viewCount;
	/**
	 * 评论数-冗余字段
	 */
	@Schema(description = "评论数-冗余字段")
	@TableField(value = "comment_count")
	private Integer commentCount;
	/**
	 * 点赞数-冗余字段
	 */
	@Schema(description = "点赞数-冗余字段")
	@TableField(value = "like_count")
	private Integer likeCount;
	/**
	 * 收藏数
	 */
	@Schema(description = "收藏数-冗余字段")
	@TableField(value = "collect_count")
	private Integer collectCount;
	/**
	 * 发布时间
	 */
//	@JSONField(serializeUsing = LocalDateTimeSerializer.class,
//			deserializeUsing = LocalDateTimeDeserializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@JsonSerialize(using = LocalDateTimeSerializer.class)  //序列化
	@JsonDeserialize(using = LocalDateTimeDeserializer.class) //方序列化
	@TableField(value = "publish_time")
	private LocalDateTime publishTime;
	/**
	 * 更新时间
	 */
//	@JSONField(serializeUsing = LocalDateTimeSerializer.class,
//			deserializeUsing = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)  //序列化
	@JsonDeserialize(using = LocalDateTimeDeserializer.class) //方序列化
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@TableField(value = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 转载地址
	 */
	@TableField(value = "reproduce")

	@Schema(description = "转载地址")
	private String reproduce;

	/**
	 * 已删除，1：是，0否
	 */
	@JsonIgnore
	@Schema(description = "是否已删除,1:是，0:否")
	@TableField(value = "deleted")
	private Integer deleted;
}