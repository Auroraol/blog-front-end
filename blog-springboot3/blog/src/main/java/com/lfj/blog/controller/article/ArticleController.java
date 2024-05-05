package com.lfj.blog.controller.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.common.validator.annotation.YearMonthFormat;
import com.lfj.blog.controller.model.request.ArticleRequest;
import com.lfj.blog.service.ArticleRecommendService;
import com.lfj.blog.service.IArticleService;
import com.lfj.blog.service.vo.ArticleArchivesVo;
import com.lfj.blog.service.vo.ArticleCategoryStatisticsVo;
import com.lfj.blog.service.vo.ArticleTagStatisticsVo;
import com.lfj.blog.service.vo.ArticleVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章表 前端控制器
 *
 * @Author: LFJ
 * @Date: 2024-04-5 12:42
 */
@RestController
@RequestMapping("/article")
@Tag(name = "文章服务", description = "/article")
public class ArticleController {

	@Autowired
	private IArticleService articleService;

	@Autowired
	private ArticleRecommendService articleRecommendService;


	@PostMapping("/save")
	@PreAuthorize("hasAuthority('admin')")
	@Operation(summary = "保存文章", description = "需要accessToken，需要管理员权限")
	public ApiResponseResult save(@Validated @RequestBody ArticleRequest request) {
		Integer id = articleService.saveOrUpdate(request);
		articleRecommendService.remove(id);        //从推荐中移除
		return ApiResponseResult.success(id);
	}


	@GetMapping("/published/page")
	@Operation(summary = "分页获取文章(已发布)", description = "用于前台页面展示,默认按发布时间倒序排序")
	public ApiResponseResult<IPage<ArticleVo>> publishedPage(@Parameter(description = "当前页，默认值：1") @RequestParam(name = "current", required = false, defaultValue = "1") long current,
															 @Parameter(description = "每页数量，默认值为：5") @RequestParam(name = "size", required = false, defaultValue = "5") long size,
															 @Parameter(description = "分类id，非必传") @RequestParam(name = "categoryId", required = false) Integer categoryId,
															 @Parameter(description = "标签id，非必传") @RequestParam(name = "tagId", required = false) Integer tagId,
															 @Parameter(description = "年月,非必传,格式:yyyy-mm") @YearMonthFormat @RequestParam(name = "yearMonth", required = false) String yearMonth,
															 @Parameter(description = "标题关键字，非必传") @RequestParam(name = "title", required = false) String title,
															 @Parameter(description = "排序字段，倒序，非必传，默认:publish_time; 可选项：发布时间:publish_time、热度:hot")
															 @RequestParam(name = "orderBy", required = false, defaultValue = "publish_time") String orderBy
	) {
		return ApiResponseResult.success(articleService.selectPublishedArticleVoPage(current, size, categoryId, tagId, yearMonth, title, orderBy));
	}


	@DeleteMapping("/discard/{id}")
	@PreAuthorize("hasAuthority('admin')")
	@Operation(summary = "丢弃文章(回收站)", description = "需要accessToken，需要管理员权限")
	public ApiResponseResult discard(@Parameter(description = "文章id") @PathVariable("id") int id) {
		articleService.discard(id);
		articleRecommendService.remove(id);
		return ApiResponseResult.success();
	}

	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('admin')")
	@Operation(summary = "删除文章", description = "逻辑删除，需要accessToken，需要管理员权限")
	public ApiResponseResult delete(@Parameter(description = "文章id") @PathVariable("id") int id) {
		articleService.delete(id);
		articleRecommendService.remove(id);
		return ApiResponseResult.success();
	}

	@GetMapping("/page")
	@PreAuthorize("hasAuthority('admin')")
	@Operation(summary = "后台管理分页获取文章", description = "可以查询所有状态的文章，用于后台管理，需要accessToken，需要管理员权限")
	public ApiResponseResult<IPage<ArticleVo>> page(@Parameter(description = "当前页") @RequestParam(name = "current", required = false, defaultValue = "1") long current,
													@Parameter(description = "每页数量") @RequestParam(name = "size", required = false, defaultValue = "5") long size,
													@Parameter(description = "文章状态,非必传，不传查全部；0:已发布，1:未发布，2:回收站") @RequestParam(name = "status", required = false) Integer status,
													@Parameter(description = "分类id，非必传") @RequestParam(name = "categoryId", required = false) Integer categoryId,
													@Parameter(description = "标签id，非必传") @RequestParam(name = "tagId", required = false) Integer tagId,
													@Parameter(description = "年月,非必传") @YearMonthFormat @RequestParam(name = "yearMonth", required = false) String yearMonth,
													@Parameter(description = "标题关键字，可空") @RequestParam(name = "title", required = false) String title) {
		return ApiResponseResult.success(articleService.selectArticleVoPage(current, size, status, title, categoryId, tagId, yearMonth));
	}

	//
	@GetMapping("/detail/{id}")
	@PreAuthorize("hasAuthority('admin')")
	@Operation(summary = "后台管理，获取文章详情信息", description = "需要accessToken,用于后台文章管理，比列表返回的多一个文章内容，文章分类列表")
	public ApiResponseResult<ArticleVo> detail(@Parameter(description = "文章id") @PathVariable("id") int id) {
		return ApiResponseResult.success(articleService.selectArticleVoById(id));
	}

	@GetMapping("/view/{id}")
	@Operation(summary = "获取文章详情信息", description = "比列表返回的多一个文章内容，文章分类列表")
	public ApiResponseResult<ArticleVo> view(@Parameter(description = "文章id") @PathVariable("id") int id) {
		ArticleVo articleVo = articleService.selectOne(id);
		return ApiResponseResult.success(articleVo);
	}

	@PutMapping("/increment_view/{id}")
	@Operation(summary = "新增浏览次数", description = "20分钟内ip或用户浏览计数")
	public ApiResponseResult incrementView(@Parameter(description = "文章id") @PathVariable("id") int id) {
		boolean viewed = articleService.incrementView(id);
		if (viewed) {
			articleRecommendService.asyncRefresh(id); // 异步刷新
		}
		return ApiResponseResult.success(viewed);
	}


	@GetMapping("/archives/page")
	@Operation(summary = "文章归档分页查询", description = "按年月归档，月份文章计数")
	public ApiResponseResult<IPage<ArticleArchivesVo>> archives(
			@Parameter(description = "当前页,非必传，默认为:1") @RequestParam(name = "current", required = false, defaultValue = "1") long current,
			@Parameter(description = "每页数量,非必传，默认为:12") @RequestParam(name = "size", required = false, defaultValue = "12") long size) {
		return ApiResponseResult.success(articleService.selectArticleArchives(current, size));
	}

	@GetMapping("/category/statistic")
	@Operation(summary = "文章分类统计", description = "按分类计数文章数")
	public ApiResponseResult<List<ArticleCategoryStatisticsVo>> categoryStatistic() {
		return ApiResponseResult.success(articleService.selectCategoryStatistic());
	}

	@GetMapping("/tag/statistic")
	@Operation(summary = "文章标签统计", description = "按标签计数文章数")
	public ApiResponseResult<List<ArticleTagStatisticsVo>> tagStatistic() {
		return ApiResponseResult.success(articleService.selectTagStatistic());
	}

	@PostMapping("/recommend/save")
	@PreAuthorize("hasAuthority('admin')")
	@Operation(summary = "添加到推荐，如果已存在则更新", description = "需要accessToken，需要管理员权限")
	public ApiResponseResult recommendAdd(@Parameter(description = "文章id") @NotNull(message = "文章id不能为空")
										  @RequestParam(name = "articleId") Integer articleId,
										  @Parameter(description = "分数，分数越高越排前面") @RequestParam(name = "score", required = false, defaultValue = "0") Double score) {
		articleRecommendService.add(articleId, score);
		return ApiResponseResult.success();
	}


	@GetMapping("/recommend/list")
	@Operation(summary = "获取文章推荐列表", description = "按分数排序")
	public ApiResponseResult<List<ArticleVo>> recommendList() {
		return ApiResponseResult.success(articleRecommendService.list());
	}


	@PreAuthorize("hasAuthority('admin')")
	@DeleteMapping("/recommend/delete/{articleId}")
	@Operation(summary = "从推荐列表中删除", description = "需要accessToken，需要管理员权限")
	public ApiResponseResult recommendDelete(@Parameter(description = "文章id") @NotNull(message = "文章id不能为空") @PathVariable(name = "articleId") Integer articleId) {
		articleRecommendService.remove(articleId);
		return ApiResponseResult.success();
	}

	@GetMapping("/interrelated/list")
	@Operation(summary = "相关文章", description = "根据分类查询，分类为空则根据标签查询")
	public ApiResponseResult<List<ArticleVo>> interrelated(@Parameter(description = "文章id") @NotNull(message = "文章id不能为空") @RequestParam(name = "articleId") Integer articleId,
														   @Parameter(description = "数量") @RequestParam(name = "limit", required = false, defaultValue = "5") Long limit
	) {
		return ApiResponseResult.success(articleService.selectInterrelatedById(articleId, limit));
	}

	@GetMapping("/count")
	@Operation(summary = "已发布文章总数")
	public ApiResponseResult<Long> count() {
		return ApiResponseResult.success(articleService.count());
	}

	@PostMapping("/status/update")
	@Operation(summary = "修改文章发布或保存状态", description = "需要accessToken，需要管理员权限")
	public ApiResponseResult status(
			@Parameter(description = "文章id") @NotNull(message = "文章id不能为空") @RequestParam("articleId") Integer articleId,
			@Parameter(description = "文章状态，0为正常，1为待发布，2为回收站") @NotNull(message = "文章状态不能为空") @RequestParam("status") Integer status
	) {
		articleService.updateStatus(articleId, status);
		articleRecommendService.asyncRefresh(articleId); //如果1为待发布, 则文章推荐也删除掉
		return ApiResponseResult.success();
	}

}
