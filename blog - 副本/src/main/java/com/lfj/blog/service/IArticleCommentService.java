package com.lfj.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lfj.blog.entity.ArticleComment;
import com.lfj.blog.service.vo.ArticleCommentVo;

import java.util.List;

/**
 * @author 16658
 * @description 针对表【article_comment(文章评论表)】的数据库操作Service
 * @createDate 2024-04-15 12:07:20
 */
public interface IArticleCommentService extends IService<ArticleComment> {
	/**
	 * 新增文章评论
	 *
	 * @param articleId
	 * @param content
	 */
	void add(Integer articleId, String content);

	/**
	 * 删除评论
	 *
	 * @param commentId
	 */
	void delete(Integer commentId);

	/**
	 * 分页查询文章评论及回复列表，包括评论者和回复者信息
	 *
	 * @param current
	 * @param size
	 * @param articleId
	 * @return
	 */
	IPage<ArticleCommentVo> selectCommentAndReplyList(long current, long size, Integer articleId);

	/**
	 * 查询最新评论，包括评论者和文章信息
	 *
	 * @param limit
	 * @return
	 */
	List<ArticleCommentVo> selectLatestComment(long limit);

	/**
	 * 异步刷新推荐列表中的评论数、发送评论提醒邮箱
	 *
	 * @param articleId
	 * @param content
	 * @return
	 */
	void asyncRefreshRecommendAndSendCommentMail(Integer articleId, String content);
}
