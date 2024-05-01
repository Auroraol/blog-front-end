package com.lfj.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 文章收藏表
 *
 * @TableName article_collect
 */
@TableName(value = "article_collect")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ArticleCollect对象", description = "文章收藏表")
public class ArticleCollect implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id")
	@TableField(value = "user_id")
	private Integer userId;
	/**
	 * 文章id
	 */
	@ApiModelProperty(value = "文章id")
	@TableField(value = "article_id")
	private Integer articleId;
}