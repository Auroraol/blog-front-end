package com.lfj.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 客户端表
 * @TableName client
 */
@TableName(value ="client")
@Data
public class Client implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 客户端id，客户端唯一标识
     */
    @TableField(value = "client_id")
    private String clientId;

    /**
     * 客户端密码
     */
    @TableField(value = "client_secret")
    private String clientSecret;

    /**
     * access_token有效时长
     */
    @TableField(value = "access_token_expire")
    private Long accessTokenExpire;

    /**
     * refresh_token_expire有效时长
     */
    @TableField(value = "refresh_token_expire")
    private Long refreshTokenExpire;

    /**
     * 是否支持刷新refresh_token,1:是，0:否
     */
    @TableField(value = "enable_refresh_token")
    private Integer enableRefreshToken;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}