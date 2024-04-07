package com.lfj.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfj.blog.common.constant.ArticleStatusEnum;
import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import com.lfj.blog.controller.model.request.ArticleRequest;
import com.lfj.blog.entity.Article;
import com.lfj.blog.exception.ApiException;
import com.lfj.blog.mapper.ArticleMapper;
import com.lfj.blog.service.IArticleService;
import com.lfj.blog.service.vo.ArticleVo;
import com.lfj.blog.service.wrapper.ArticlePageQueryWrapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 16658
 * @description 针对表【article(文章表)】的数据库操作Service实现
 * @createDate 2024-04-05 22:30:03
 */
@Service
public class IArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
		implements IArticleService {

	@Autowired
	ArticleMapper articleMapper;

	/**
	 * 新增或更新文章
	 *
	 * @param request
	 */
	@Override
	public Integer saveOrUpdate(ArticleRequest request) {
		Integer status = request.getStatus();
		if (!status.equals(0) & !status.equals(1)) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "无效状态码");
		}
		Article article = new Article();
		// 文章id处理
		Integer id = request.getId() == null ? null : request.getId() == 0 ? null : request.getId();
		article.setId(id);
		// 判断是否原创，做不同处理
//		setAuthor(article, request);
		// 获取分类信息
//		Category category = categoryService.getById(request.getCategoryId());
//		if (category == null) {
//			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "分类不存在");
//		}
//		article.setCategoryName(category.getName());
//		article.setCategoryId(category.getId());
//		// 文章标题、摘要、内容、封面
//		article.setTitle(request.getTitle());
//		article.setSummary(request.getSummary());
//		article.setContent(request.getContent());
//		article.setCover(request.getCover());
//		article.setHtmlContent(request.getHtmlContent());
//		// 文章状态
//		article.setStatus(status);
//		// 时间
//		if (ArticleStatusEnum.NORMAL.getStatus().equals(status) || id == null) {
//			article.setPublishTime(LocalDateTime.now());
//		}
//		article.setUpdateTime(LocalDateTime.now());
//		// 保存或更新文章
//		saveOrUpdate(article);
//		Integer articleId = article.getId();
//		// 文章-标签 关联
//		List<Integer> tagIds = request.getTagIds();
//		// 如果标签列表为空列表，则全部删除原文字标签
//		if (CollectionUtils.isEmpty(tagIds)) {
//			deleteArticleTagByArticleId(articleId);
//			return articleId;
//		}
//		// 判断传的标签id列表中的id是否存在该标签
//		QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
//		queryWrapper.lambda().in(Tag::getId, tagIds);
//		int count = tagService.count(queryWrapper);
//		if (count == 0) {
//			throw new ApiException(ErrorEnum.INVALID_REQUEST.getErrorCode(), "标签id不存在");
//		}
//		// 数据库文字标签列表
//		List<ArticleTag> artTagList = articleTagService.list(new QueryWrapper<ArticleTag>().lambda().eq(ArticleTag::getArticleId, articleId));
//		// 请求的文章标签列表
//		List<ArticleTag> reqTagList = tagIds.stream().map((tagId) -> new ArticleTag(articleId, tagId)).collect(Collectors.toList());
//		// 标签是否不变
//		if (artTagList.containsAll(reqTagList) && reqTagList.containsAll(artTagList)) {
//			return articleId;
//		}
//		// 非逻辑删除-删除原来的 && 批量新增文章标签
//		deleteArticleTagByArticleId(articleId);
//		articleTagService.saveBatch(reqTagList);
//		return articleId;
		return null;
	}

	/**
	 * 分页查询文章
	 *
	 * @param current
	 * @param size
	 * @param status
	 * @param title
	 * @param categoryId
	 * @param tagId
	 * @param yearMonth
	 * @return
	 */
	@Override
	public IPage<ArticleVo> selectArticleVoPage(long current, long size, Integer status, String title, Integer categoryId, Integer tagId, String yearMonth) {
		validStatus(status);
		String[] startAndEndOfMonth = getStartAndEndOfMonth(yearMonth);
		String start = startAndEndOfMonth[0];
		String end = startAndEndOfMonth[1];
		int count = selectPageCount(status, categoryId, tagId, start, end, title);
		if (count == 0) {
			return new Page<>(current, size);
		}
		ArticlePageQueryWrapper queryWrapper = new ArticlePageQueryWrapper();
		queryWrapper.setOffset((current - 1) * size);
		queryWrapper.setLimit(size);
		queryWrapper.setCategoryId(categoryId);
		queryWrapper.setTagId(tagId);
		queryWrapper.setTitle(title);
		queryWrapper.setOrderBy("publish_time");
		queryWrapper.setStart(start);
		queryWrapper.setEnd(end);
		queryWrapper.setStatus(status);
		List<ArticleVo> articleVoList = articleMapper.selectArticleVoPage(queryWrapper);
		Page<ArticleVo> page = new Page<>(current, size, count);
		page.setRecords(articleVoList);
		return page;
	}

	/**
	 * 分页查询已发布文章
	 *
	 * @param current
	 * @param size
	 * @param categoryId
	 * @param tagId
	 * @param yearMonth  归档年月，因为数据库函数date_format会使publish_time索引失效，索引转换下再查询
	 * @param title
	 * @param orderBy    排序
	 * @return
	 */
	@Override
	public IPage<ArticleVo> selectPublishedArticleVoPage(long current, long size,
														 Integer categoryId, Integer tagId,
														 String yearMonth, String title, String orderBy) {
		// 查询start-end时期的文章
		String[] startAndEndOfMonth = getStartAndEndOfMonth(yearMonth);
		String start = startAndEndOfMonth[0];
		String end = startAndEndOfMonth[1];
		int count = selectPageCount(ArticleStatusEnum.NORMAL.getStatus(), categoryId, tagId, start, end, title);
		if (count == 0) {
			return new Page<>(current, size);
		}
		//自定义Wrapper
		ArticlePageQueryWrapper queryWrapper = new ArticlePageQueryWrapper();
		queryWrapper.setOffset((current - 1) * size);
		queryWrapper.setLimit(size);
		queryWrapper.setCategoryId(categoryId);
		queryWrapper.setTagId(tagId);
		queryWrapper.setTitle(title);
		queryWrapper.setOrderBy(orderBy);
		queryWrapper.setStart(start);
		queryWrapper.setEnd(end);
		queryWrapper.setStatus((ArticleStatusEnum.NORMAL.getStatus()));  // 文章状态正常的
		List<ArticleVo> articleVoList = articleMapper.selectArticleVoPage(queryWrapper);
		Page<ArticleVo> page = new Page<>(current, size, count);
		page.setRecords(articleVoList);
		return page;
	}

	/**
	 * 文章计数
	 *
	 * @param status     状态
	 * @param categoryId 分类id
	 * @param tagId      标签id
	 * @param start      开始日期
	 * @param end        结束日期
	 * @param title      标题关键字
	 * @return
	 */
	private int selectPageCount(Integer status, Integer categoryId, Integer tagId, String start, String end, String title) {
		return articleMapper.selectPageCount(status, categoryId, tagId, start, end, title);
	}


	/**
	 * 根据年月获取字符串获取对应月份开始时间和结束时间
	 *
	 * @param yearMonth
	 * @return
	 */
	private String[] getStartAndEndOfMonth(String yearMonth) {
		if (StringUtils.isBlank(yearMonth)) {
			return new String[]{null, null};
		}
		String separator = "-";
		String[] array = yearMonth.split(separator);
		String yearStr = array[0];
		String monthStr = array[1];
		// 如：2020-04-02 00:00
		String start = yearStr + separator + monthStr + separator + "01" + " 00:00";
		int nextMonth = Integer.parseInt(monthStr) + 1;
		// 如：2020-05-01 00:00
		String end = yearStr + separator + nextMonth + separator + "01" + " 00:00";
		return new String[]{start, end};
	}

	/**
	 * 文章状态码校验
	 *
	 * @param status
	 */
	private void validStatus(Integer status) {
		if (status == null) {
			return;
		}
		Integer[] array = {0, 1, 2};
		if (!ArrayUtils.contains(array, status)) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "无效状态码");
		}
	}
}





