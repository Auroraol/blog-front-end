package com.entity.sys;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.baomidou.mybatisplus.annotation.TableName;
import com.common.basic.entity.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("sys_user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysUser extends BaseEntity{

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 角色id
     */
    private String roles;

    /**
     * 登录名称
     */
    private String userName;

    /**
     * 用户姓名
     */
    private String realName;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码盐值
     */
    private String salt;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 帐号状态（0正常 1停用 2锁定）
     */
    private int status;

    /**
     * 最后登录时间
     */
    private Date loginDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 登录错误次数
     */
    private int errorNum;

    /**
     * 密码更改时间
     */
    private Date updatePwdTime;

    private transient SysDept dept;

    private transient List<SysRole> roleList;
}
