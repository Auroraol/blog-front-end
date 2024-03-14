package com.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.common.basic.entity.BaseEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@TableName("sys_dept")
public class SysDept extends BaseEntity {

    /**
     * 父部门id
     */
    private Long parentId;

    private transient String parentName;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 类型 1 公司 2 部门
     */
    private int deptType;

    /**
     * 部门状态（0正常 1停用）
     */
    private int status;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 子节点
     */
    private transient List<SysDept> children = new ArrayList<SysDept>();
}
