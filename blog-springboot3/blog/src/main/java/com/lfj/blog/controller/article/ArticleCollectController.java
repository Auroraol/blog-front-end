package com.lfj.blog.controller.article;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.service.ArticleRecommendService;
import com.lfj.blog.service.IArticleCollectService;
import com.lfj.blog.service.vo.ArticleVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 文章收藏表 前端控制器
 *
 * @Author: LFJ
 * @Date: 2024-04-12 16:42
 */
@RestController
@RequestMapping("/article/collect")
@Tag(name = "文章收藏服务", description = "/article/collect")
public class ArticleCollectController {

	@Autowired
	private IArticleCollectService articleCollectService;

	@Autowired
	private ArticleRecommendService articleRecommendService;

	@PostMapping("/add")
	@Operation(summary = "新增收藏", description = "需要accessToken")
	public ApiResponseResult add(@Parameter(description = "文章id") @NotNull(message = "文章id不能为空") @RequestParam(value = "articleId") Integer articleId) {
		articleCollectService.add(articleId);
		articleRecommendService.asyncRefresh(articleId);
		return ApiResponseResult.success();
	}

	@DeleteMapping("/delete")
	@Operation(summary = "删除收藏", description = "需要accessToken")
	public ApiResponseResult delete(@Parameter(description = "文章id") @NotNull(message = "文章id不能为空") @RequestParam("articleId") Integer articleId) {
		articleCollectService.delete(articleId);
		articleRecommendService.asyncRefresh(articleId);
		return ApiResponseResult.success();
	}

	@GetMapping("/page")
	@Operation(summary = "分页查询收藏文章", description = "需要accessToken")
	public ApiResponseResult<IPage<ArticleVo>> page(
			@Parameter(description = "当前页，默认值：1") @RequestParam(value = "current", required = false, defaultValue = "1") long current,
			@Parameter(description = "每页数量，默认值为：5") @RequestParam(value = "size", required = false, defaultValue = "5") long size) {
		return ApiResponseResult.success(articleCollectService.page(current, size));
	}

	@GetMapping("/collected/{articleId}")
	@Operation(summary = "查询文章是否已收藏，1：是，0：否", description = "需要accessToken")
	public ApiResponseResult<Integer> collected(@Parameter(description = "文章id") @PathVariable("articleId") Integer articleId) {
		return ApiResponseResult.success(articleCollectService.collected(articleId));
	}


}
