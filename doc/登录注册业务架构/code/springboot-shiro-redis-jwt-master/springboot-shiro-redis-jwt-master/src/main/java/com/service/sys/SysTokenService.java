package com.service.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.sys.SysToken;
import com.mapper.sys.SysTokenMapper;
import org.springframework.stereotype.Service;

@Service
public class SysTokenService extends ServiceImpl<SysTokenMapper, SysToken> {

    /**
     * 根据token获取token信息
     */
    public SysToken getByToken(String token){
        QueryWrapper<SysToken> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("token",token);
        return baseMapper.selectOne(queryWrapper);
    }

}
