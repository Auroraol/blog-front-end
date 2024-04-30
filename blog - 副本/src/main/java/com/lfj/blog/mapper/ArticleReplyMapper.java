package com.lfj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lfj.blog.entity.ArticleReply;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 16658
 * @description 针对表【article_reply(文章回复表)】的数据库操作Mapper
 * @createDate 2024-04-17 16:04:42
 * @Entity com.lfj.blog.entity.ArticleReply
 */

@Mapper
public interface ArticleReplyMapper extends BaseMapper<ArticleReply> {

}




