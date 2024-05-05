package com.lfj.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 标签表
 *
 * @TableName tag
 */
@TableName(value = "tag")
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "Tag对象", description = "标签表")
public class Tag implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	@Null(message = "不需要传id")
	@Schema(description = "id")
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * 标签名
	 */
	@NotBlank(message = "标签名不能为空")
	@Schema(description = "标签名")
	@TableField(value = "name")
	private String name;
	/**
	 * 已删除,1:是，0否
	 */
	@Schema(description = "是否已删除,1:是，0:否")
	@TableField(value = "deleted")
	private Integer deleted;
}