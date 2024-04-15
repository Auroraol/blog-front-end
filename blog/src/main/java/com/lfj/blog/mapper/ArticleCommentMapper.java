package com.lfj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lfj.blog.entity.ArticleComment;
import com.lfj.blog.service.vo.ArticleCommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 16658
 * @description 针对表【article_comment(文章评论表)】的数据库操作Mapper
 * @createDate 2024-04-15 12:07:20
 * @Entity com.lfj.blog.entity.ArticleComment
 */

@Mapper
public interface ArticleCommentMapper extends BaseMapper<ArticleComment> {
	/**
	 * 查询文章评论及回复列表，包括评论者和回复者信息
	 *
	 * @param articleId
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<ArticleCommentVo> selectCommentAndReplyList(@Param("offset") long offset, @Param("limit") long limit, @Param("articleId") Integer articleId);

	/**
	 * 查询最新评论，包括评论者和文章信息
	 *
	 * @param limit
	 * @return
	 */
	List<ArticleCommentVo> selectLatestComment(@Param("limit") long limit);
}




