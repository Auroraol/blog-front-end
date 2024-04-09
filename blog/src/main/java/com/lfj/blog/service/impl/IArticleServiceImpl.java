package com.lfj.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfj.blog.common.constant.ArticleStatusEnum;
import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import com.lfj.blog.common.security.ServerSecurityContext;
import com.lfj.blog.common.security.details.CustomUserDetails;
import com.lfj.blog.controller.model.dto.PreArtAndNextArtDTO;
import com.lfj.blog.controller.model.request.ArticleRequest;
import com.lfj.blog.entity.Article;
import com.lfj.blog.entity.ArticleTag;
import com.lfj.blog.entity.Category;
import com.lfj.blog.entity.Tag;
import com.lfj.blog.exception.ApiException;
import com.lfj.blog.mapper.ArticleMapper;
import com.lfj.blog.service.IArticleService;
import com.lfj.blog.service.IArticleTagService;
import com.lfj.blog.service.ICategoryService;
import com.lfj.blog.service.ITagService;
import com.lfj.blog.service.vo.ArticleVo;
import com.lfj.blog.service.wrapper.ArticlePageQueryWrapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

	@Lazy
	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private ITagService tagService;

	@Autowired
	private IArticleTagService articleTagService;

	/**
	 * 新增或更新文章
	 *
	 * @param request
	 */
	@Override
	public Integer saveOrUpdate(ArticleRequest request) {
		// 文章表操作
		Integer status = request.getStatus();
		if (!status.equals(0) & !status.equals(1)) {
			// 0为正常，1为待发布
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "无效状态码");
		}
		Article article = new Article();
		// 文章id处理 如果 request.getId() 的值为 null 或 0，那么 id 的值将为 null，否则 id 的值将与 request.getId() 的值相同。
		Integer id = (request.getId() != null && request.getId() != 0) ? request.getId() : null;
		article.setId(id);
		// 判断是否原创，做不同处理
		this.setAuthor(article, request);
		//获取分类信息
		Category category = categoryService.getById(request.getCategoryId());
		if (category == null) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "分类不存在");
		}
		article.setCategoryName(category.getName());
		article.setCategoryId(category.getId());
		// 文章标题、摘要、内容、封面
		article.setTitle(request.getTitle());
		article.setSummary(request.getSummary());
		article.setContent(request.getContent());
		article.setCover(request.getCover());
		article.setHtmlContent(request.getHtmlContent());
		// 文章状态
		article.setStatus(status);
		// 时间
		if (ArticleStatusEnum.NORMAL.getStatus().equals(status) || id == null) {
			article.setPublishTime(LocalDateTime.now());
		}
		article.setUpdateTime(LocalDateTime.now());
		// 保存或更新文章
		this.saveOrUpdate(article);  //mybatis提供的

		// 标签表 和 文章-标签 操作
		Integer articleId = article.getId();
		List<Integer> tagIds = request.getTagIds();
		// 如果标签列表为空列表，表示准备清空标签, 则删除原文字标签. 返回articleId, 不用操作文章-标签表了
		if (CollectionUtils.isEmpty(tagIds)) {
			this.deleteArticleTagByArticleId(articleId);
			return articleId;
		}

		// 标签表操作
		// 判断请求传的标签id列表, 在表中的id是否存在
		QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().in(Tag::getId, tagIds);
		int count = (int) tagService.count(queryWrapper);
		if (count == 0) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "标签id不存在");
		}
		// 文章-标签表操作
		// 数据库文章标签列表
		List<ArticleTag> artTagList = articleTagService.list(new QueryWrapper<ArticleTag>()
				.lambda().eq(ArticleTag::getArticleId, articleId));
		// 请求的文章标签列表 tagIds
		List<ArticleTag> reqTagList = tagIds.stream().map((tagId) ->
				new ArticleTag(articleId, tagId)).collect(Collectors.toList());
		// 标签是否不变
		if (artTagList.containsAll(reqTagList) && reqTagList.containsAll(artTagList)) {
			return articleId;
		}

		// 批量新增文章标签
		deleteArticleTagByArticleId(articleId);  // 删除原来的
		articleTagService.saveBatch(reqTagList); // 更新
		return articleId;
	}

	/**
	 * 根据文章id删除文章标签
	 *
	 * @param articleId
	 */
	private void deleteArticleTagByArticleId(Integer articleId) {
		QueryWrapper<ArticleTag> deleteWrapper = new QueryWrapper<>();
		deleteWrapper.lambda().eq(ArticleTag::getArticleId, articleId);
		articleTagService.remove(deleteWrapper);
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
		queryWrapper.setOrderBy("publish_time");  //发布时间
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
		//自定义查询Wrapper
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
		// 1. 创建分页对象, 即当前页码数和每页显示的记录数
		Page<ArticleVo> page = new Page<>(current, size, count);
		page.setRecords(articleVoList);
		return page;
	}

	/**
	 * 文章详情(后台)
	 *
	 * @param id
	 * @return
	 */
	@Override
	public ArticleVo selectArticleVoById(int id) {
		ArticleVo articleVo = this.baseMapper.selectArticleVoById(id, null);
		if (articleVo == null) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "文章不存在");
		}
		List<Category> categoryList = categoryService.parentList(articleVo.getCategoryId());
		articleVo.setCategoryList(categoryList);
		return articleVo;
	}

	/**
	 * 文章详情(前台)
	 *
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ArticleVo selectOne(int id) {
		ArticleVo articleVo = this.baseMapper.selectArticleVoById(id, ArticleStatusEnum.NORMAL.getStatus());
//		System.out.println(articleVo.getContent());
		if (articleVo == null) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "文章不存在");
		}
		// 详情不返回原内容
		articleVo.setContent(null);
		// 上一篇和下一篇
		PreArtAndNextArtDTO preAndNext = selectPreAndNext(id);
		articleVo.setPrevious(preAndNext.getPre());
		articleVo.setNext(preAndNext.getNext());
		return articleVo;
	}

	/**
	 * 新增浏览次数
	 *
	 * @param id
	 */
	@Override
	public boolean incrementView(int id) {
//		CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(false);
//		String key = userDetail != null ? RedisConstant.ART_VIEW + userDetail.getId() + ":" + id
//				: RedisConstant.ART_VIEW + IpUtil.getIpAddress() + ":" + id;
//		Boolean notViewed = stringRedisTemplate.opsForValue().setIfAbsent(key, "viewed", 120L, TimeUnit.MINUTES);
//		if (notViewed != null && notViewed) {
//			// 浏览次数自增
//			Article article = getById(id);
//			if (article != null) {
//				Article newArticle = new Article();
//				newArticle.setId(id);
//				newArticle.setViewCount(article.getViewCount() + 1);
//				updateById(newArticle);
//				return true;
//			}
//		}
		return false;
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
	 * 查询上一篇和下一篇
	 *
	 * @param id
	 * @return
	 */
	private PreArtAndNextArtDTO selectPreAndNext(int id) {
		List<Article> articleList = this.baseMapper.selectPreAndNext(id);
		int two = 2;
		int size = articleList.size();
		PreArtAndNextArtDTO dto = new PreArtAndNextArtDTO();
		if (size == two) {
			dto.setPre(articleList.get(0));
			dto.setNext(articleList.get(1));
		} else if (size == 1) {
			oneHandle(dto, articleList.get(0), id);
		}
		return dto;
	}

	/**
	 * 只有一篇文章判断是上一篇还是下一篇
	 *
	 * @param dto
	 * @param one
	 * @param id
	 */
	private void oneHandle(PreArtAndNextArtDTO dto, Article one, Integer id) {
		Integer oneId = one.getId();
		if (oneId > id) {
			dto.setNext(one);
		} else {
			dto.setPre(one);
		}
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
	 * 作者设置
	 *
	 * @param article
	 * @param request
	 * @return
	 */
	private void setAuthor(Article article, ArticleRequest request) {
		// 是否原创
		Integer original = request.getOriginal();  // 是否原创
		String reproduce = request.getReproduce(); // 转载地址
		if (original.equals(0) && StringUtils.isBlank(reproduce)) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "转载地址不能为空");
		}
		if (original.equals(0)) {
			//如果是转载
			article.setReproduce(reproduce);  //保存转载地址
		} else if (original.equals(1)) {
			//如果是原创
			article.setReproduce(null);
		} else {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "无效转载标识");
		}
		// 获取任务上下文, 得到当前用户信息
		CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
		article.setUserId(userDetail.getId());
		article.setOriginal(original);
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





