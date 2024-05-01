package com.lfj.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfj.blog.common.constant.CommonConstant;
import com.lfj.blog.common.constant.RoleConstant;
import com.lfj.blog.common.constant.UserConstant;
import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import com.lfj.blog.common.security.ServerSecurityContext;
import com.lfj.blog.common.security.details.CustomUserDetails;
import com.lfj.blog.entity.LeaveMessage;
import com.lfj.blog.entity.User;
import com.lfj.blog.exception.ApiException;
import com.lfj.blog.mapper.LeaveMessageMapper;
import com.lfj.blog.service.ILeaveMessageService;
import com.lfj.blog.service.IUserService;
import com.lfj.blog.service.security.biz.AsyncService;
import com.lfj.blog.service.security.biz.EmailService;
import com.lfj.blog.service.vo.LeaveMessageVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 16658
 * @description 针对表【leave_message(留言表)】的数据库操作Service实现
 * @createDate 2024-04-16 15:01:52
 */
@Service
public class ILeaveMessageServiceImpl extends ServiceImpl<LeaveMessageMapper, LeaveMessage>
		implements ILeaveMessageService {
	@Autowired
	LeaveMessageMapper leaveMessageMapper;
	@Autowired
	private IUserService userService;
	@Autowired
	private AsyncService asyncService;
	@Autowired
	private EmailService emailService;
	@Value("${mail.message}")
	private String url;

	/**
	 * 新增留言
	 *
	 * @param content
	 */
	@Override
	public void add(String content) {
		LeaveMessage leaveMessage = new LeaveMessage();
		CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
		leaveMessage.setFromUserId(userDetail.getId());
		leaveMessage.setContent(content);
		leaveMessage.setCreateTime(LocalDateTime.now());
		leaveMessage.setDeleted(CommonConstant.NOT_DELETED);
		save(leaveMessage);
		// 异步发送留言提醒邮件给管理员
		asyncSendEmailToAdmin(content, userDetail.getNickname());
	}

	/**
	 * 留言回复
	 *
	 * @param pid
	 * @param toUserId
	 * @param content
	 */
	@Override
	public void reply(Integer pid, Integer toUserId, String content) {
		LeaveMessage leaveMessage = new LeaveMessage();
		leaveMessage.setPid(pid);
		// 任务上下文
		CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
		leaveMessage.setFromUserId(userDetail.getId());
		leaveMessage.setToUserId(toUserId);
		leaveMessage.setContent(content);
		leaveMessage.setCreateTime(LocalDateTime.now());
		leaveMessage.setDeleted(CommonConstant.NOT_DELETED);
		save(leaveMessage);
		// 异步发送留言回复提醒邮件
		asyncSendEmailToTargetAndAdmin(toUserId, content);
	}


	/**
	 * 分页获取留言及回复列表
	 *
	 * @param current
	 * @param size
	 * @return
	 */
	@Override
	public IPage<LeaveMessageVo> page(long current, long size) {
		QueryWrapper<LeaveMessage> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().isNull(LeaveMessage::getPid);
		long count = count(queryWrapper);
		if (count == 0) {
			return new Page<>(current, size);
		}
		List<LeaveMessageVo> records = leaveMessageMapper.selectLeaveMessageAndReplyList((current - 1) * size, size);
		Page<LeaveMessageVo> page = new Page<>(current, size, count);
		page.setRecords(records);
		return page;
	}

	/**
	 * 最新留言
	 *
	 * @param limit
	 * @return
	 */
	@Override
	public List<LeaveMessageVo> selectLatest(long limit) {
		return leaveMessageMapper.selectLatest(limit);
	}

	/**
	 * 删除（本人和管理可删除）
	 *
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		LeaveMessage message = getById(id);
		if (message != null) {
			CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
			List<String> roleList = userDetail.getRoles();
			// 不是本人，也不是管理员不允许删除
			if (!message.getFromUserId().equals(userDetail.getId()) & !roleList.contains(RoleConstant.ADMIN)) {
				throw new ApiException(ResponseCodeEnum.PERMISSION_DENIED.getCode(), ResponseCodeEnum.PERMISSION_DENIED.getMessage());
			}
			removeById(id);
		}
	}

	/**
	 * 异步送邮件给管理员
	 *
	 * @param content
	 */
	private void asyncSendEmailToAdmin(String content, String nickname) {
		asyncService.runAsync((r) -> sendEmailToAdmin(content, nickname));
	}


	/**
	 * 异步发送邮箱给指定用户并抄送到管理员
	 *
	 * @param toUserId
	 * @param content
	 * @return
	 */
	private void asyncSendEmailToTargetAndAdmin(Integer toUserId, String content) {
		asyncService.runAsync(r -> sendEmailToTargetAndAdmin(toUserId, content));
	}

	/**
	 * 发送邮箱给指定用户并抄送到管理员
	 *
	 * @param toUserId
	 * @param content
	 * @return
	 */
	private Boolean sendEmailToTargetAndAdmin(Integer toUserId, String content) {
		User user = userService.getById(toUserId);

		if (user != null && !StringUtils.isBlank(user.getEmail())) {
			QueryWrapper<User> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().eq(User::getAdmin, UserConstant.ADMIN);
			List<User> userList = userService.list(queryWrapper);
			// 过滤掉邮箱为空的管理员用户和当前用户（有可能当前用户也是管理员）
			// 剩下的邮箱去重
			List<String> adminEmailList = userList.stream().filter((u) -> !StringUtils.isBlank(u.getEmail()) && !u.getId().equals(toUserId))
					.map(User::getEmail).distinct().collect(Collectors.toList());
			int size = adminEmailList.size();
			Map<String, Object> params = new HashMap<>(3);
			params.put("url", url);
			params.put("nickname", user.getNickname());
			params.put("content", content);
			String topic = "留言回复提醒";
			emailService.sendHtmlMail(user.getEmail(), topic, "message_reply", params, adminEmailList.toArray(new String[size]));
		}
		return Boolean.TRUE;
	}

	/**
	 * 发送邮件给管理员
	 */
	private Boolean sendEmailToAdmin(String content, String nickname) {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(User::getAdmin, UserConstant.ADMIN);
		List<User> userList = userService.list(queryWrapper);
		// 过滤掉邮箱为空的管理员用户 去重
		List<String> adminEmailList = userList.stream().filter((u) -> !StringUtils.isBlank(u.getEmail())).map(User::getEmail).distinct().collect(Collectors.toList());
		if (!adminEmailList.isEmpty()) {
			String to = adminEmailList.get(0);
			adminEmailList.remove(to);
			Map<String, Object> params = new HashMap<>(3);
			params.put("url", url);
			params.put("nickname", nickname);
			params.put("content", content);
			String topic = "留言提醒";
			int size = adminEmailList.size();
			emailService.sendHtmlMail(to, topic, "message", params, adminEmailList.toArray(new String[size]));
		}
		return Boolean.TRUE;
	}
}




