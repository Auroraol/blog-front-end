package com.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.sys.SysUser;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    List<SysUser> list(Integer offset,Integer pageSize,SysUser user);
}
