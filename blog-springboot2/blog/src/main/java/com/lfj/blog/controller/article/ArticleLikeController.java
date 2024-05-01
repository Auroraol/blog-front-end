package com.lfj.blog.controller.article;


import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.service.ArticleRecommendService;
import com.lfj.blog.service.IArticleLikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 文章点赞表 前端控制器
 *
 * @Author: LFJ
 * @Date: 2024-04-12 16:02
 */
@RestController
@RequestMapping("/article/like")
@Api(tags = "文章点赞服务", value = "/article/like")
public class ArticleLikeController {

	@Autowired
	private IArticleLikeService articleLikeService;

	@Autowired
	private ArticleRecommendService articleRecommendService;


	@GetMapping("/liked/{articleId}")
	@ApiOperation(value = "查询文章是否已点赞", notes = "1：是，0：否")
	public ApiResponseResult<Integer> liked(@ApiParam("文章id") @PathVariable("articleId") Integer articleId) {
		return ApiResponseResult.success(articleLikeService.liked(articleId));
	}

	@PostMapping("/add")
	@ApiOperation(value = "文章点赞")
	public ApiResponseResult like(@ApiParam("文章id") @RequestParam("articleId") Integer articleId) {
		articleLikeService.like(articleId);
		articleRecommendService.asyncRefresh(articleId);
		return ApiResponseResult.success();
	}

	@DeleteMapping("/cancel")
	@ApiOperation(value = "取消文章点赞")
	public ApiResponseResult cancel(@ApiParam("文章id") @NotNull(message = "文章id不能为空") @RequestParam("articleId") Integer articleId) {
		articleLikeService.cancel(articleId);
		articleRecommendService.asyncRefresh(articleId);
		return ApiResponseResult.success();
	}

}
