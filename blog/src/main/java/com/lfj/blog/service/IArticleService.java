package com.lfj.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lfj.blog.controller.model.request.ArticleRequest;
import com.lfj.blog.entity.Article;
import com.lfj.blog.service.vo.ArticleVo;

/**
 * @author 16658
 * @description 针对表【article(文章表)】的数据库操作Service
 * @createDate 2024-04-05 22:30:03
 */
public interface IArticleService extends IService<Article> {

	/**
	 * 保存文章
	 *
	 * @param request
	 * @return 返回文章id
	 */
	Integer saveOrUpdate(ArticleRequest request);


	/**
	 * 分页查询文章
	 *
	 * @param current
	 * @param size
	 * @param status
	 * @param title
	 * @param categoryId
	 * @param tagId
	 * @param yearMonth
	 * @return
	 */
	IPage<ArticleVo> selectArticleVoPage(long current, long size, Integer status, String title, Integer categoryId, Integer tagId, String yearMonth);


	/**
	 * 分页查询已发布文章
	 *
	 * @param current
	 * @param size
	 * @param categoryId
	 * @param tagId
	 * @param yearMonth
	 * @param title
	 * @param orderBy
	 * @return
	 */
	IPage<ArticleVo> selectPublishedArticleVoPage(long current, long size, Integer categoryId, Integer tagId, String yearMonth, String title, String orderBy);

	/**
	 * 文章详细(后台)
	 *
	 * @param id
	 * @return
	 */
	ArticleVo selectArticleVoById(int id);

	/**
	 * 文章详情(前台)
	 *
	 * @param id
	 * @return
	 */
	ArticleVo selectOne(int id);

	/**
	 * 新增浏览次数
	 *
	 * @param id
	 * @return
	 */
	boolean incrementView(int id);
}
