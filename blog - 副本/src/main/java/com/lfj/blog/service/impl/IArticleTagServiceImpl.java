package com.lfj.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfj.blog.entity.ArticleTag;
import com.lfj.blog.mapper.ArticleTagMapper;
import com.lfj.blog.service.IArticleTagService;
import org.springframework.stereotype.Service;

/**
 * @author 16658
 * @description 针对表【article_tag(文章-标签 关联表)】的数据库操作Service实现
 * @createDate 2024-04-08 12:01:53
 */
@Service
public class IArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
		implements IArticleTagService {

}




