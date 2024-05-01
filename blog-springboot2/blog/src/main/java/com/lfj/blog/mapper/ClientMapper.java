package com.lfj.blog.mapper;

import com.lfj.blog.entity.Client;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 16658
* @description 针对表【client(客户端表)】的数据库操作Mapper
* @createDate 2024-03-21 20:46:08
* @Entity com.lfj.blog.entity.Client
*/

@Mapper
public interface ClientMapper extends BaseMapper<Client> {

}




