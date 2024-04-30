package com.lfj.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfj.blog.common.constant.CommonConstant;
import com.lfj.blog.common.constant.RoleConstant;
import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import com.lfj.blog.common.security.ServerSecurityContext;
import com.lfj.blog.common.security.details.CustomUserDetails;
import com.lfj.blog.entity.ArticleReply;
import com.lfj.blog.entity.User;
import com.lfj.blog.exception.ApiException;
import com.lfj.blog.mapper.ArticleReplyMapper;
import com.lfj.blog.service.IArticleReplyService;
import com.lfj.blog.service.IUserService;
import com.lfj.blog.service.security.biz.AsyncService;
import com.lfj.blog.service.security.biz.EmailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 16658
 * @description 针对表【article_reply(文章回复表)】的数据库操作Service实现
 * @createDate 2024-04-17 16:04:43
 */
@Service
public class IArticleReplyServiceImpl extends ServiceImpl<ArticleReplyMapper, ArticleReply>
		implements IArticleReplyService {
	@Autowired
	private AsyncService asyncService;

	@Autowired
	private IUserService userService;

	@Autowired
	private EmailService emailService;

	@Value("${mail.article}")
	private String prefix;

	/**
	 * 新增文章评论回复
	 *
	 * @param articleId
	 * @param commentId
	 * @param toUserId
	 * @param content
	 */
	@Override
	public void add(Integer articleId, Integer commentId, Integer toUserId, String content) {
		ArticleReply reply = new ArticleReply();
		reply.setArticleId(articleId);
		reply.setCommentId(commentId);
		reply.setToUserId(toUserId);
		reply.setContent(content);
		CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
		reply.setFromUserId(userDetail.getId());
		reply.setReplyTime(LocalDateTime.now());
		reply.setDeleted(CommonConstant.NOT_DELETED);
		save(reply);
	}

	/**
	 * 异步发送回复提醒邮箱
	 *
	 * @param articleId
	 * @param toUserId
	 * @param content
	 * @return
	 */
	@Override
	public void asyncSendMail(Integer articleId, Integer toUserId, String content) {
		asyncService.runAsync((s) -> sendMail(articleId, toUserId, content));
	}


	/**
	 * 删除回复
	 *
	 * @param replyId
	 */
	@Override
	public void delete(Integer replyId) {
		ArticleReply reply = getById(replyId);
		if (reply != null) {
			Integer fromUserId = reply.getFromUserId();
			// 上下文
			CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
			List<String> roleList = userDetail.getRoles();
			// 不是本人，也不是管理员不允许删除
			if (!fromUserId.equals(userDetail.getId()) & !roleList.contains(RoleConstant.ADMIN)) {
				throw new ApiException(ResponseCodeEnum.PERMISSION_DENIED.getCode(), ResponseCodeEnum.PERMISSION_DENIED.getMessage());
			}
			removeById(replyId);
		}
	}

	/**
	 * 发送回复提醒邮箱
	 *
	 * @param articleId
	 * @param toUserId
	 * @param content
	 * @return
	 */
	private Boolean sendMail(Integer articleId, Integer toUserId, String content) {
		User user = userService.getById(toUserId);
		if (user != null && !StringUtils.isBlank(user.getEmail())) {
			Map<String, Object> params = new HashMap<>(3);
			prefix = prefix.endsWith("/") ? prefix : prefix + "/";
			params.put("url", prefix + articleId);
			params.put("nickname", user.getNickname());
			params.put("content", content);
			emailService.sendHtmlMail(user.getEmail(), "回复提醒", "article_reply", params);
		}
		return Boolean.TRUE;
	}
}




