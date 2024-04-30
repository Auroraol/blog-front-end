package com.lfj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lfj.blog.entity.Article;
import com.lfj.blog.service.vo.ArticleArchivesVo;
import com.lfj.blog.service.vo.ArticleCategoryStatisticsVo;
import com.lfj.blog.service.vo.ArticleTagStatisticsVo;
import com.lfj.blog.service.vo.ArticleVo;
import com.lfj.blog.service.wrapper.ArticlePageQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 16658
 * @description 针对表【article(文章表)】的数据库操作Mapper
 * @createDate 2024-04-05 22:30:03
 * @Entity com.lfj.blog.entity.Article
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

	/**
	 * 分页查询
	 *
	 * @param queryWrapper
	 * @return
	 */
	List<ArticleVo> selectArticleVoPage(ArticlePageQueryWrapper queryWrapper);


	/**
	 * 分页查询计数
	 *
	 * @param status     状态
	 * @param categoryId 分类id
	 * @param tagId      标签id
	 * @param start      开始日期
	 * @param end        结束日期
	 * @param title      标题关键字
	 * @return
	 */
	Integer selectPageCount(@Param("status") Integer status,
							@Param("categoryId") Integer categoryId,
							@Param("tagId") Integer tagId,
							@Param("start") String start,
							@Param("end") String end,
							@Param("title") String title);


	/**
	 * 根据文章id查询
	 *
	 * @param id     文章id
	 * @param status
	 * @return
	 */
	ArticleVo selectArticleVoById(@Param("id") int id, @Param("status") Integer status);

	/**
	 * 查询上一篇和下一篇
	 *
	 * @param id
	 * @return
	 */
	List<Article> selectPreAndNext(@Param("id") int id);


	/**
	 * 文章归档计数
	 *
	 * @return
	 */
	Integer selectArticleArchivesCount();

	/**
	 * 文章归档
	 *
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<ArticleArchivesVo> selectArticleArchives(@Param("offset") long offset, @Param("limit") long limit);

	/**
	 * 按分类计数文章数
	 *
	 * @return
	 */
	List<ArticleCategoryStatisticsVo> selectCategoryStatistic();

	/**
	 * 按标签计数文章数
	 *
	 * @return
	 */
	List<ArticleTagStatisticsVo> selectTagStatistic();

	/**
	 * 标签列表查询文章列表
	 *
	 * @param tagList
	 * @param limit
	 * @return
	 */
	List<ArticleVo> selectByTagList(@Param("tagList") List<Integer> tagList, @Param("limit") long limit);

	/**
	 * 点赞数自增
	 *
	 * @param id
	 */
	void likeCountIncrement(@Param("id") int id);

	/**
	 * 点赞数自减
	 *
	 * @param id
	 */
	void likeCountDecrement(@Param("id") int id);

	/**
	 * 评论数自增
	 *
	 * @param id
	 */
	void commentCountIncrement(@Param("id") int id);

	/**
	 * 评论数自减
	 *
	 * @param id
	 */
	void commentCountDecrement(@Param("id") int id);

	/**
	 * 收藏数自增
	 *
	 * @param id
	 */
	void collectCountIncrement(@Param("id") int id);

	/**
	 * 收藏数自减
	 *
	 * @param id
	 */
	void collectCountDecrement(@Param("id") int id);

	/**
	 * 分页查询用户收藏文章
	 *
	 * @param offset
	 * @param limit
	 * @param userId
	 * @return
	 */
	List<ArticleVo> selectCollectByUserId(@Param("offset") long offset, @Param("limit") long limit, @Param("userId") Integer userId);
}





