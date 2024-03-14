package com.service.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.sys.SysRole;
import com.mapper.sys.SysRoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> {

    public List<SysRole> roleListByUser(String roles){
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("FIND_IN_SET(id,'"+roles+"')");
        queryWrapper.select("id,role_name,role_key");
        return baseMapper.selectList(queryWrapper);
    }
}
