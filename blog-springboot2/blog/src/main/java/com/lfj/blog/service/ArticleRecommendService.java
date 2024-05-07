package com.lfj.blog.service;


import com.lfj.blog.service.vo.ArticleVo;

import java.util.List;

/**
 * 文章推荐服务接口
 * 热门文章，访问量比较大的，也放进redis缓存里
 * 文章推荐基于redis的zset实现，数据直接存储在redis中，按分数顺序排序，及score可当作是排序字段。
 **/
public interface ArticleRecommendService {

	/**
	 * 新增推荐
	 *
	 * @param articleId
	 * @param score     分数
	 */
	void add(Integer articleId, Double score);

	/**
	 * 获取推荐列表
	 *
	 * @return
	 */
	List<ArticleVo> list();

	/**
	 * 从推荐中移除
	 *
	 * @param articleId
	 */
	boolean remove(Integer articleId);

	/**
	 * 异步刷新
	 *
	 * @param articleId
	 */
	void asyncRefresh(Integer articleId);

	/**
	 * 刷新
	 *
	 * @param articleId
	 */
	void refresh(Integer articleId);
}
