package com.lfj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lfj.blog.entity.ArticleCollect;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 16658
 * @description 针对表【article_collect(文章收藏表)】的数据库操作Mapper
 * @createDate 2024-04-12 16:43:24
 * @Entity com.lfj.blog.entity.ArticleCollect
 */

@Mapper
public interface ArticleCollectMapper extends BaseMapper<ArticleCollect> {

}




