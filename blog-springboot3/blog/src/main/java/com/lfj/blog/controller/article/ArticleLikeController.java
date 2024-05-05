package com.lfj.blog.controller.article;


import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.service.ArticleRecommendService;
import com.lfj.blog.service.IArticleLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章点赞表 前端控制器
 *
 * @Author: LFJ
 * @Date: 2024-04-12 16:02
 */
@RestController
@RequestMapping("/article/like")
@Tag(name = "文章点赞服务", description = "/article/like")
public class ArticleLikeController {

	@Autowired
	private IArticleLikeService articleLikeService;

	@Autowired
	private ArticleRecommendService articleRecommendService;


	@GetMapping("/liked/{articleId}")
	@Operation(summary = "查询文章是否已点赞", description = "1：是，0：否")
	public ApiResponseResult<Integer> liked(@Parameter(description = "文章id") @PathVariable("articleId") Integer articleId) {
		return ApiResponseResult.success(articleLikeService.liked(articleId));
	}

	@PostMapping("/add")
	@Operation(summary = "文章点赞")
	public ApiResponseResult like(@Parameter(description = "文章id") @RequestParam("articleId") Integer articleId) {
		articleLikeService.like(articleId);
		articleRecommendService.asyncRefresh(articleId);
		return ApiResponseResult.success();
	}

	@DeleteMapping("/cancel")
	@Operation(summary = "取消文章点赞")
	public ApiResponseResult cancel(@Parameter(description = "文章id") @NotNull(message = "文章id不能为空") @RequestParam("articleId") Integer articleId) {
		articleLikeService.cancel(articleId);
		articleRecommendService.asyncRefresh(articleId);
		return ApiResponseResult.success();
	}

}
