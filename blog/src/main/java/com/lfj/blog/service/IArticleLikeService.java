package com.lfj.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lfj.blog.entity.ArticleLike;

/**
 * @author 16658
 * @description 针对表【article_like(文章点赞表)】的数据库操作Service
 * @createDate 2024-04-12 16:08:41
 */
public interface IArticleLikeService extends IService<ArticleLike> {

	/**
	 * 查询文章是否已点赞
	 *
	 * @param articleId
	 * @return 1：是，0：否
	 */
	Integer liked(Integer articleId);

	/**
	 * 文章点赞
	 *
	 * @param articleId
	 */
	void like(Integer articleId);

	/**
	 * 取消文章点赞
	 *
	 * @param articleId
	 */
	void cancel(Integer articleId);

}
