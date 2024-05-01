package com.lfj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lfj.blog.entity.ArticleLike;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 16658
 * @description 针对表【article_like(文章点赞表)】的数据库操作Mapper
 * @createDate 2024-04-12 16:08:40
 * @Entity com.lfj.blog.entity.ArticleLike
 */
@Mapper
public interface ArticleLikeMapper extends BaseMapper<ArticleLike> {

}




