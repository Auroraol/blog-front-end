package com.lfj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lfj.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 16658
 * @description 针对表【user】的数据库操作Mapper
 * @createDate 2024-03-09 17:46:34
 * @Entity com.lfj.blog.entity.User
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {

}




