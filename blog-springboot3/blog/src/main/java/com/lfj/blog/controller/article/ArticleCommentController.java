package com.lfj.blog.controller.article;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.service.IArticleCommentService;
import com.lfj.blog.service.vo.ArticleCommentVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章评论表 前端控制器
 *
 * @Author: LFJ
 * @Date: 2024-04-15 15:22
 */
@RestController
@RequestMapping("/article/comment")
@Tag(name = "文章评论服务", description = "/article/comment")
public class ArticleCommentController {

	@Autowired
	private IArticleCommentService articleCommentService;


	@PostMapping("/add")
	@Operation(summary = "新增文章评论", description = "需要accessToken")
	public ApiResponseResult add(@Parameter(description = "文章id") @NotNull(message = "文章id不能为空") @RequestParam(value = "articleId") Integer articleId,
								 @Parameter(description = "评论内容") @NotBlank(message = "评论内容不能为空") @RequestParam(value = "content") String content) {
		articleCommentService.add(articleId, content);
		articleCommentService.asyncRefreshRecommendAndSendCommentMail(articleId, content);
		return ApiResponseResult.success();
	}

	@DeleteMapping("/delete")
	@Operation(summary = "删除文章评论", description = "逻辑删除，需要accessToken,管理员或评论发表者可删除")
	public ApiResponseResult delete(@Parameter(description = "评论id") @NotNull(message = "评论id不能为空") @RequestParam(value = "commentId") Integer commentId) {
		articleCommentService.delete(commentId);
		return ApiResponseResult.success();
	}

	@GetMapping("/page")
	@Operation(summary = "分页获取文章评论及回复列表", description = "包括评论者和回复者信息")
	public ApiResponseResult<IPage<ArticleCommentVo>> page(
			@Parameter(description = "页码") @RequestParam(value = "current", required = false, defaultValue = "1") long current,
			@Parameter(description = "每页数量") @RequestParam(value = "size", required = false, defaultValue = "5") long size,
			@Parameter(description = "文章id") @NotNull(message = "文章id不能为空") @RequestParam(value = "articleId") Integer articleId) {
		return ApiResponseResult.success(articleCommentService.selectCommentAndReplyList(current, size, articleId));
	}

	@GetMapping("/latest")
	@Operation(summary = "最新文章评论列表", description = "包括文章信息和评论者信息")
	public ApiResponseResult<List<ArticleCommentVo>> latest(
			@Parameter(description = "数量限制,默认值:5") @RequestParam(value = "limit", required = false, defaultValue = "5") long limit) {
		return ApiResponseResult.success(articleCommentService.selectLatestComment(limit));
	}
}
