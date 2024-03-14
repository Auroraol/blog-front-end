package com.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.common.basic.entity.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_dict")
public class SysDict extends BaseEntity {

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 字典代码
     */
    private String dictCode;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典值
     */
    private String dictValue;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;
}
