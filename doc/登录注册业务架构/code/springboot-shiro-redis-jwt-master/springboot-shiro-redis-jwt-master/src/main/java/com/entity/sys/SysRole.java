package com.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.common.basic.entity.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_role")
public class SysRole extends BaseEntity {

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色代码
     */
    private String roleKey;

    /**
     * 备注
     */
    private String remark;
}
