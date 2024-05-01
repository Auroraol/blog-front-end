package com.lfj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lfj.blog.entity.OauthUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 16658
 * @description 针对表【oauth_user(第三方登录关联表)】的数据库操作Mapper
 * @createDate 2024-03-22 18:14:16
 * @Entity com.lfj.blog.entity.OauthUser
 */
@Mapper
public interface OauthUserMapper extends BaseMapper<OauthUser> {

}




