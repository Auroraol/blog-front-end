package com.lfj.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfj.blog.common.constant.CommonConstant;
import com.lfj.blog.common.constant.RoleConstant;
import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import com.lfj.blog.common.security.ServerSecurityContext;
import com.lfj.blog.common.security.details.CustomUserDetails;
import com.lfj.blog.entity.Article;
import com.lfj.blog.entity.ArticleComment;
import com.lfj.blog.entity.User;
import com.lfj.blog.exception.ApiException;
import com.lfj.blog.mapper.ArticleCommentMapper;
import com.lfj.blog.service.IArticleCommentService;
import com.lfj.blog.service.IArticleService;
import com.lfj.blog.service.IUserService;
import com.lfj.blog.service.security.biz.AsyncService;
import com.lfj.blog.service.security.biz.EmailService;
import com.lfj.blog.service.vo.ArticleCommentVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 16658
 * @description 针对表【article_comment(文章评论表)】的数据库操作Service实现
 * @createDate 2024-04-15 12:07:20
 */
@Service
public class IArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleComment>
		implements IArticleCommentService {
	@Autowired
	ArticleCommentMapper articleCommentMapper;

//	@Autowired
//	private ArticleRecommendService recommendService;
	@Autowired
	private IArticleService articleService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private IUserService userService;
	@Autowired
	private AsyncService asyncService;
	@Value("${mail.article}")
	private String prefix;

	/**
	 * 新增文章评论
	 *
	 * @param articleId
	 * @param content
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void add(Integer articleId, String content) {
		ArticleComment comment = new ArticleComment();
		comment.setArticleId(articleId);
		comment.setContent(content);
		comment.setCommentTime(LocalDateTime.now());
		// 用户上下文
		CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
		//
		comment.setFromUserId(userDetail.getId());
		comment.setDeleted(CommonConstant.NOT_DELETED);
		save(comment); // 保存到数据库
		articleService.commentCountIncrement(articleId); //文章评论数自增
	}

	/**
	 * 异步刷新推荐列表中的评论数、发送评论提醒邮件
	 *
	 * @param articleId
	 * @param content
	 * @return
	 */
	@Override
	public void asyncRefreshRecommendAndSendCommentMail(Integer articleId, String content) {
		asyncService.runAsync((s) -> refreshRecommendAndSendCommentMail(articleId, content));
	}

	/**
	 * 删除评论(逻辑删除),发表评论者和管理员可删除
	 *
	 * @param commentId
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Integer commentId) {
		ArticleComment comment = getById(commentId);
		if (comment != null) {
			CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
			List<String> roleList = userDetail.getRoles();
			// 不是本人，也不是管理员不允许删除
			if (!comment.getFromUserId().equals(userDetail.getId())
					& !roleList.contains(RoleConstant.ADMIN)) {
				throw new ApiException(ResponseCodeEnum.PERMISSION_DENIED.getCode(), "无权删除");
			}
			removeById(commentId);
			Integer articleId = comment.getArticleId();
			articleService.commentCountDecrement(articleId); //文章评论数自减
//			recommendService.asyncRefresh(articleId);
		}
	}

	/**
	 * 查询文章评论及回复列表，包括评论者和回复者信息
	 *
	 * @param articleId
	 * @return
	 */
	@Override
	public IPage<ArticleCommentVo> selectCommentAndReplyList(long current, long size, Integer articleId) {
		QueryWrapper<ArticleComment> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(ArticleComment::getArticleId, articleId);
		long count = count(queryWrapper);
		if (count == 0) {
			return new Page<>(current, size);
		}
		List<ArticleCommentVo> records = articleCommentMapper.selectCommentAndReplyList((current - 1) * size, size, articleId);
		Page<ArticleCommentVo> page = new Page<>(current, size, count);
		page.setRecords(records);
		return page;
	}

	/**
	 * 查询最新评论，包括评论者和文章信息
	 *
	 * @param limit
	 * @return
	 */
	@Override
	public List<ArticleCommentVo> selectLatestComment(long limit) {
		return articleCommentMapper.selectLatestComment(limit);
	}

	/**
	 * 刷新推荐列表中的评论数、发送评论提醒邮件
	 *
	 * @param articleId
	 * @param content
	 * @return
	 */
	private Boolean refreshRecommendAndSendCommentMail(Integer articleId, String content) {
//		recommendService.refresh(articleId);
		Article article = articleService.getById(articleId);
		if (article != null) {
			Integer userId = article.getUserId();
			User user = userService.getById(userId);
			if (user != null && !StringUtils.isBlank(user.getEmail())) {
				Map<String, Object> params = new HashMap<>(3);
				prefix = prefix.endsWith("/") ? prefix : prefix + "/";
				params.put("url", prefix + articleId);
				params.put("nickname", user.getNickname());
				params.put("content", content);
				String topic = "评论提醒";
				emailService.sendHtmlMail(user.getEmail(), topic, "article_comment", params);
			}
		}
		return Boolean.TRUE;
	}

}




