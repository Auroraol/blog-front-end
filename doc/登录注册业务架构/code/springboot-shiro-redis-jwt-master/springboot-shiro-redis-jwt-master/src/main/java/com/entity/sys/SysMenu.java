package com.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.common.basic.entity.BaseEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@TableName("sys_menu")
public class SysMenu extends BaseEntity {

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 父菜单id
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 地址
     */
    private String url;

    /**
     * 菜单类型（m目录 c菜单 f按钮）
     */
    private String type;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 备注
     */
    private String remark;

    /**
     * 子节点
     */
    private transient List<SysMenu> children = new ArrayList<>();
}
