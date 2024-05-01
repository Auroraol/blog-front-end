package com.lfj.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 留言表
 *
 * @TableName leave_message
 */
@TableName(value = "leave_message")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "LeaveMessage对象", description = "留言表")
public class LeaveMessage implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * 父id
	 */
	@ApiModelProperty(value = "父id")
	@TableField(value = "pid")
	private Integer pid;
	/**
	 * 留言者id
	 */
	@ApiModelProperty(value = "留言者id")
	@TableField(value = "from_user_id")
	private Integer fromUserId;
	/**
	 *
	 */
	@ApiModelProperty(value = "被回复者id")
	@TableField(value = "to_user_id")
	private Integer toUserId;
	/**
	 * 内容
	 */
	@ApiModelProperty(value = "内容")
	@TableField(value = "content")
	private String content;
	/**
	 * 时间
	 */
	@ApiModelProperty(value = "时间")
	@TableField(value = "create_time")
	private LocalDateTime createTime;
	/**
	 * 是否删除，1：是，0：否
	 */
	@ApiModelProperty(value = "是否删除，1：是，0：否")
	@TableField(value = "deleted")
	private Integer deleted;
}