package com.lfj.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lfj.blog.entity.ArticleCollect;
import com.lfj.blog.service.vo.ArticleVo;

/**
 * @author 16658
 * @description 针对表【article_collect(文章收藏表)】的数据库操作Service
 * @createDate 2024-04-12 16:43:24
 */
public interface IArticleCollectService extends IService<ArticleCollect> {
	/**
	 * 新增收藏
	 *
	 * @param articleId
	 */
	void add(Integer articleId);

	/**
	 * 删除收藏
	 *
	 * @param articleId
	 */
	void delete(Integer articleId);

	/**
	 * 分页查询用户收藏文章
	 *
	 * @param current
	 * @param size
	 * @return
	 */
	IPage<ArticleVo> page(long current, long size);

	/**
	 * 文章是否收藏
	 *
	 * @param articleId
	 * @return
	 */
	Integer collected(Integer articleId);
}
