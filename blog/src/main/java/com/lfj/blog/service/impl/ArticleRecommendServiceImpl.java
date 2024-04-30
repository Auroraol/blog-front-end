package com.lfj.blog.service.impl;

import com.lfj.blog.common.constant.ArticleStatusEnum;
import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import com.lfj.blog.exception.ApiException;
import com.lfj.blog.mapper.ArticleMapper;
import com.lfj.blog.service.ArticleRecommendService;
import com.lfj.blog.service.vo.ArticleVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 文章推荐服务
 * 文章推荐基于redis的zset实现，数据直接存储在redis中，按分数顺序排序，及score可当作是排序字段。
 **/
@Log4j2
@Service
public class ArticleRecommendServiceImpl implements ArticleRecommendService {

	private static final String KEY = "art:recommend:";

	@Autowired
	private ZSetOperations<String, Object> zSetOperations;

	@Autowired
	private ArticleMapper articleMapper;

	/**
	 * 新增推荐
	 *
	 * @param articleId
	 * @param score     文章分数
	 */
	@Override
	public void add(Integer articleId, Double score) {
		if (articleId == null) {
			return;
		}
		ArticleVo articleVo = articleMapper.selectArticleVoById(articleId, ArticleStatusEnum.NORMAL.getStatus());
		if (articleVo == null) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "文章不存在或未发布");
		}
		if (!articleVo.getStatus().equals(0)) {
			return;
		}
		// 先移除原有的
		remove(articleId);
		// 只存列表所需要字段
		articleVo.setContent(null);
		articleVo.setNext(null);
		articleVo.setPrevious(null);
		articleVo.setCategoryList(null);
		zSetOperations.add(KEY, articleVo, score);  // 存入Redis
	}

	/**
	 * 获取推荐列表
	 *
	 * @return
	 */
	@Override
	public List<ArticleVo> list() {
		Set<ZSetOperations.TypedTuple<Object>> valueScoreSet =
				zSetOperations.rangeWithScores(KEY, 0, -1);  // 取出所以
		List<ArticleVo> resultList = new ArrayList<>();
		if (valueScoreSet != null) {
			valueScoreSet.forEach(item -> {
				ArticleVo articleVo = (ArticleVo) item.getValue();
				if (articleVo != null) {
					articleVo.setRecommendScore(item.getScore());  //推荐分数
					resultList.add(articleVo);
				}
			});
		}
		return resultList;
	}

	/**
	 * 从推荐中移除
	 *
	 * @param articleId
	 */
	@Override
	public void remove(Integer articleId) {
		if (articleId == null) {
			return;
		}
		Set<Object> set = zSetOperations.range(KEY, 0, -1);
		if (set != null) {
			set.forEach(i -> {
				ArticleVo articleVo = (ArticleVo) i;
				if (articleId.equals(articleVo.getId())) {
					zSetOperations.remove(KEY, articleVo);
				}
			});
		}
	}

	/**
	 * 刷新
	 *
	 * @param articleId
	 */
	@Override
	public void refresh(Integer articleId) {
		asyncRefresh(articleId);
	}

	/**
	 * 异步刷新(底层使用aop，所以同一个类调用异步不会生效)
	 * <p>
	 * 从一个排序集合中移除具有特定 articleId 的元素，并且根据这个 articleId 添加新的元素
	 *
	 * @param articleId
	 */
	@Async
	@Override
	public void asyncRefresh(Integer articleId) {
		if (articleId == null) {
			return;
		}
		ArticleVo articleVo;
		Set<ZSetOperations.TypedTuple<Object>> valueScoreSet =
				zSetOperations.reverseRangeWithScores(KEY, 0, -1);
		if (valueScoreSet != null) {
			for (ZSetOperations.TypedTuple<Object> item : valueScoreSet) {
				ArticleVo value = (ArticleVo) item.getValue();
				if (articleId.equals(value.getId())) {
					articleVo = value;
					double score = item.getScore();
					zSetOperations.remove(KEY, articleVo);
					add(articleId, score);
				}
			}
		}
	}
}
