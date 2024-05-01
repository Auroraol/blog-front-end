package com.lfj.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import com.lfj.blog.common.security.ServerSecurityContext;
import com.lfj.blog.common.security.details.CustomUserDetails;
import com.lfj.blog.entity.ArticleCollect;
import com.lfj.blog.exception.ApiException;
import com.lfj.blog.mapper.ArticleCollectMapper;
import com.lfj.blog.service.IArticleCollectService;
import com.lfj.blog.service.IArticleService;
import com.lfj.blog.service.vo.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 16658
 * @description 针对表【article_collect(文章收藏表)】的数据库操作Service实现
 * @createDate 2024-04-12 16:43:24
 */
@Service
public class IArticleCollectServiceImpl extends ServiceImpl<ArticleCollectMapper, ArticleCollect>
		implements IArticleCollectService {
	@Autowired
	private IArticleService articleService;

	/**
	 * 新增收藏
	 *
	 * @param articleId
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void add(Integer articleId) {
		CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
		QueryWrapper<ArticleCollect> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(ArticleCollect::getUserId, userDetail.getId()).eq(ArticleCollect::getArticleId, articleId);
		int count = (int) count(queryWrapper);
		if (count != 0) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "文章已收藏，不可重复收藏");
		}
		ArticleCollect collect = new ArticleCollect();
		collect.setArticleId(articleId);
		collect.setUserId(userDetail.getId());
		save(collect);
		// 文章冗余字段，收藏数自增
		articleService.collectCountIncrement(articleId);
	}

	/**
	 * 删除收藏
	 *
	 * @param articleId
	 */
	@Override
	public void delete(Integer articleId) {
		QueryWrapper<ArticleCollect> queryWrapper = new QueryWrapper<>();
		CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
		queryWrapper.lambda().eq(ArticleCollect::getUserId, userDetail.getId()).eq(ArticleCollect::getArticleId, articleId);
		int count = (int) count(queryWrapper);
		if (count == 0) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "文章未收藏");
		}
		remove(queryWrapper);
		// 文章冗余字段，收藏数自减
		articleService.collectCountDecrement(articleId);
	}


	/**
	 * 文章是否收藏
	 *
	 * @param articleId
	 * @return
	 */
	@Override
	public Integer collected(Integer articleId) {
		QueryWrapper<ArticleCollect> queryWrapper = new QueryWrapper<>();
		CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
		queryWrapper.lambda().eq(ArticleCollect::getUserId, userDetail.getId()).eq(ArticleCollect::getArticleId, articleId);
		return Math.toIntExact(count(queryWrapper));
	}

	/**
	 * 分页查询用户收藏文章
	 *
	 * @param current
	 * @param size
	 * @return
	 */
	@Override
	public IPage<ArticleVo> page(long current, long size) {
		CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
		Integer userId = userDetail.getId();
		QueryWrapper<ArticleCollect> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(ArticleCollect::getUserId, userId);
		int count = (int) count(queryWrapper);
		if (count == 0) {
			return new Page<>(current, size);
		}
		List<ArticleVo> articleVoList = articleService.selectCollectByUserId((current - 1) * size, size, userId);
		Page<ArticleVo> page = new Page<>(current, size, count);
		page.setRecords(articleVoList);
		return page;
	}
}




