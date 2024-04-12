package com.lfj.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import com.lfj.blog.common.security.ServerSecurityContext;
import com.lfj.blog.common.security.details.CustomUserDetails;
import com.lfj.blog.entity.ArticleLike;
import com.lfj.blog.exception.ApiException;
import com.lfj.blog.mapper.ArticleLikeMapper;
import com.lfj.blog.service.IArticleLikeService;
import com.lfj.blog.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 16658
 * @description 针对表【article_like(文章点赞表)】的数据库操作Service实现
 * @createDate 2024-04-12 16:08:40
 */
@Service
public class IArticleLikeServiceImpl extends ServiceImpl<ArticleLikeMapper, ArticleLike>
		implements IArticleLikeService {

	@Autowired
	private IArticleService articleService;

	/**
	 * 查询文章该用户是否已点赞
	 *
	 * @param articleId
	 * @return 1：是，0：否
	 */
	@Override
	public Integer liked(Integer articleId) {
		// 任务上下文得到当前用户信息
		CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
		// 文章点赞关联表
		QueryWrapper<ArticleLike> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(ArticleLike::getArticleId, articleId)
				.eq(ArticleLike::getUserId, userDetail.getId());
		return Math.toIntExact(count(queryWrapper));
	}

	/**
	 * 文章点赞
	 *
	 * @param articleId
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void like(Integer articleId) {
		// 任务上下文得到当前用户信息
		CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
		Integer userId = userDetail.getId();
		// 文章点赞关联表
		QueryWrapper<ArticleLike> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(ArticleLike::getArticleId, articleId).eq(ArticleLike::getUserId, userId);
		int count = (int) count(queryWrapper);
		if (count != 0) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "文章已点赞，不可重复点赞");
		}
		// 文章点赞表-新增
		ArticleLike like = new ArticleLike();
		like.setArticleId(articleId);
		like.setUserId(userId);
		save(like);
		// 文章表, 点赞计数增加
		articleService.likeCountIncrement(articleId);

	}

	/**
	 * 取消文章点赞
	 *
	 * @param articleId
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void cancel(Integer articleId) {
		QueryWrapper<ArticleLike> queryWrapper = new QueryWrapper<>();
		CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
		queryWrapper.lambda().eq(ArticleLike::getArticleId, articleId).eq(ArticleLike::getUserId, userDetail.getId());
		int count = (int) count(queryWrapper);
		if (count == 0) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "文章未点赞");
		}
		// 文章点赞表-删除
		remove(queryWrapper);
		// 文章表, 点赞计数减少
		articleService.likeCountDecrement(articleId);
	}
}




