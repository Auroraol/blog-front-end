package com.lfj.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 分类表
 *
 * @TableName category
 */
@TableName(value = "category")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "Category对象", description = "分类表")
public class Category implements Serializable {
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
	 * 父类id
	 */
	@Schema(description = "父类id")
	@TableField(value = "parent_id")
	private Integer parentId;
	/**
	 * 已删除，1：是，0：否
	 */
	@JsonIgnore
	@TableLogic
	@Schema(description = "是否已删除,1:是，0:否")
	@TableField(value = "deleted")
	private Integer deleted;
}