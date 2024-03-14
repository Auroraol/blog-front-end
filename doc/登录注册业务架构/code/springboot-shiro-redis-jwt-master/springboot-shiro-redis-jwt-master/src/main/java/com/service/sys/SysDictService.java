package com.service.sys;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.sys.SysDict;
import com.mapper.sys.SysDictMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class SysDictService extends ServiceImpl<SysDictMapper, SysDict> {

    @Autowired
    private SysDictMapper dictMapper;



}
