package com.lfj.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lfj.blog.entity.ArticleReply;

/**
 * @author 16658
 * @description 针对表【article_reply(文章回复表)】的数据库操作Service
 * @createDate 2024-04-17 16:04:43
 */
public interface IArticleReplyService extends IService<ArticleReply> {
	/**
	 * 新增文章评论回复
	 *
	 * @param articleId
	 * @param commentId
	 * @param toUserId
	 * @param content
	 */
	void add(Integer articleId, Integer commentId, Integer toUserId, String content);

	/**
	 * 删除回复
	 *
	 * @param replyId
	 */
	void delete(Integer replyId);

	/**
	 * 异步发送回复提醒邮箱
	 *
	 * @param articleId
	 * @param toUserId
	 * @param content
	 * @return
	 */
	void asyncSendMail(Integer articleId, Integer toUserId, String content);
}
