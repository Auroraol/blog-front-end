package com.lfj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lfj.blog.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 16658
 * @description 针对表【article_tag(文章-标签 关联表)】的数据库操作Mapper
 * @createDate 2024-04-08 12:01:53
 * @Entity com.lfj.blog.entity.ArticleTag
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

}




