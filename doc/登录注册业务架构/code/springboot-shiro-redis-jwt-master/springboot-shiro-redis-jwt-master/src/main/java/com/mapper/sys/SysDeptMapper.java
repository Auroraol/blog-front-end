package com.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.sys.SysDept;

import java.util.List;

public interface SysDeptMapper extends BaseMapper<SysDept> {

    List<SysDept> deptList(SysDept dept);
}
