package com.service.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.sys.SysUser;
import com.mapper.sys.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    @Autowired
    private SysUserMapper userMapper;

    public SysUser getUserByPass(String userName,String password){
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",userName);
        queryWrapper.eq("password",password);
        return baseMapper.selectOne(queryWrapper);
    }

    public SysUser getUserByName(String userName){
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",userName);
        return userMapper.selectOne(queryWrapper);
    }

    public List<SysUser> list(Integer offset, Integer pageSize, SysUser user){
        return userMapper.list(offset,pageSize,user);
    }

    public String getSalt(String userName){
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",userName);
        queryWrapper.select("salt");
        List<Object> obj = baseMapper.selectObjs(queryWrapper);
        String salt = obj.get(0).toString();
        return salt;
    }
}
