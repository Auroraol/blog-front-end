package com.lfj.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 第三方登录关联表
 *
 * @TableName oauth_user
 */
@TableName(value = "oauth_user")
@Data
@ApiModel(value = "OauthUser对象", description = "第三方登录关联表")
public class OauthUser implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 第三方平台的用户唯一id
     */
    @TableField(value = "uuid")
    @ApiModelProperty(value = "第三方平台的用户唯一id")
    private String uuid;
    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    /**
     * 认证类型，1：qq，2：github，3：微信，4：gitee
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "认证类型，1：qq，2：github，3：微信，4：gitee")
    private Integer type;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}