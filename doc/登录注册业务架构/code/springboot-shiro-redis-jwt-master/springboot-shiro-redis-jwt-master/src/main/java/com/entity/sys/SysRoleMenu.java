package com.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_role")
public class SysRoleMenu {
    private Long roleId;
    private Long menuId;
}
