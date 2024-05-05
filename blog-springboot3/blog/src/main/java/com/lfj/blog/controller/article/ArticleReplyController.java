package com.lfj.blog.controller.article;


import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.service.IArticleReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 文章回复表 前端控制器
 */
@RestController
@RequestMapping("/article/reply")
@Tag(name = "文章评论回复服务", description = "/article/reply")
public class ArticleReplyController {

	@Autowired
	private IArticleReplyService articleReplyService;

	@PostMapping("/add")
	@Operation(summary = "新增文章评论回复", description = "需要accessToken")
	public ApiResponseResult add(@Parameter(description = "文章id") @NotNull(message = "文章id不能为空") @RequestParam(value = "articleId") Integer articleId,
								 @Parameter(description = "评论id") @NotNull(message = "评论id不能为空") @RequestParam(value = "commentId") Integer commentId,
								 @Parameter(description = "被回复者id") @NotNull(message = "被回复者id不能为空") @RequestParam(value = "toUserId") Integer toUserId,
								 @Parameter(description = "回复内容") @NotBlank(message = "回复内容不能为空") @RequestParam(value = "content") String content) {
		articleReplyService.add(articleId, commentId, toUserId, content);
		articleReplyService.asyncSendMail(articleId, toUserId, content);  //发送邮件
		return ApiResponseResult.success();
	}

	@DeleteMapping("/delete")
	@Operation(summary = "删除回复", description = "逻辑删除，需要accessToken,管理员或回复者可删除")
	public ApiResponseResult delete(@Parameter(description = "回复id") @NotNull(message = "回复id不能为空") @RequestParam(value = "replyId") Integer replyId) {
		articleReplyService.delete(replyId);
		return ApiResponseResult.success();
	}
}
