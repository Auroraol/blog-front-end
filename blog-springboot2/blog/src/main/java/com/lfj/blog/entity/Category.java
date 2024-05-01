package com.lfj.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "Category对象", description = "分类表")
public class Category implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @TableField(value = "name")
    private String name;
    /**
     * 父类id
     */
    @ApiModelProperty(value = "父类id")
    @TableField(value = "parent_id")
    private Integer parentId;
    /**
     * 已删除，1：是，0：否
     */
    @JsonIgnore
    @TableLogic
    @ApiModelProperty(value = "是否已删除,1:是，0:否")
    @TableField(value = "deleted")
    private Integer deleted;
}