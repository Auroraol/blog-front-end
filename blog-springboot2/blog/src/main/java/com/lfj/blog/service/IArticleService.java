package com.lfj.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lfj.blog.controller.model.request.ArticleRequest;
import com.lfj.blog.entity.Article;
import com.lfj.blog.service.vo.ArticleArchivesVo;
import com.lfj.blog.service.vo.ArticleCategoryStatisticsVo;
import com.lfj.blog.service.vo.ArticleTagStatisticsVo;
import com.lfj.blog.service.vo.ArticleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

	/**
	 * 相关文章查询
	 * 通过文章分类查询同一分类的文章
	 *
	 * @param id
	 * @param limit
	 * @return
	 */
	List<ArticleVo> selectInterrelatedById(Integer id, Long limit);


	/**
	 * 点赞数自增
	 *
	 * @param articleId
	 */
	void likeCountIncrement(int articleId);

	/**
	 * 点赞数自减
	 *
	 * @param articleId
	 */
	void likeCountDecrement(int articleId);


	/**
	 * 收藏数自增
	 *
	 * @param articleId
	 */
	void collectCountIncrement(int articleId);

	/**
	 * 收藏数自减
	 *
	 * @param articleId
	 */
	void collectCountDecrement(int articleId);

	/**
	 * 分页查询用户收藏文章
	 *
	 * @param offset
	 * @param limit
	 * @param userId
	 * @return
	 */
	List<ArticleVo> selectCollectByUserId(@Param("offset") long offset, @Param("limit") long limit, @Param("userId") Integer userId);

	/**
	 * 评论数自增
	 *
	 * @param articleId
	 */
	void commentCountIncrement(int articleId);

	/**
	 * 评论数自减
	 *
	 * @param articleId
	 */
	void commentCountDecrement(int articleId);

	/**
	 * 分页年月归档查询
	 *
	 * @param current
	 * @param size
	 * @return
	 */
	IPage<ArticleArchivesVo> selectArticleArchives(long current, long size);


	/**
	 * 丢弃文章
	 *
	 * @param id
	 */
	void discard(int id);

	/**
	 * 删除文章（逻辑删除）
	 *
	 * @param id
	 */
	void delete(int id);

	/**
	 * 按分类计数文章数(mapper)
	 *
	 * @return
	 */
	List<ArticleCategoryStatisticsVo> selectCategoryStatistic();

	/**
	 * 按标签计数文章数(mapper)
	 *
	 * @return
	 */
	List<ArticleTagStatisticsVo> selectTagStatistic();

	/**
	 * 更新文章状态
	 *
	 * @param articleId
	 * @param status    0或1
	 */
	void updateStatus(Integer articleId, Integer status);

}
