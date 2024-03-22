package com.lfj.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfj.blog.entity.OauthUser;
import com.lfj.blog.mapper.OauthUserMapper;
import com.lfj.blog.service.IOauthUserService;
import org.springframework.stereotype.Service;

/**
 * @author 16658
 * @description 针对表【oauth_user(第三方登录关联表)】的数据库操作Service实现
 * @createDate 2024-03-22 18:14:16
 */
@Service
public class IOauthUserServiceImpl extends ServiceImpl<OauthUserMapper, OauthUser>
		implements IOauthUserService {

}




