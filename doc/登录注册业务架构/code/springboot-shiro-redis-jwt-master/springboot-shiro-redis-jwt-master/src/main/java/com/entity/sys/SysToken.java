package com.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_token")
public class SysToken {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户token
     */
    private String token;

    /**
     * 终端类型（1 web端 2 app端）
     */
    private Integer type;

    /**
     * 登录状态 （1 已登录 2 已注销）
     */
    private Integer status;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 退出时间
     */
    private Date logoutTime;

    /**
     * 最后一次登录时间（最后一次请求时间）
     */
    private Date lastRequestTime;
}
