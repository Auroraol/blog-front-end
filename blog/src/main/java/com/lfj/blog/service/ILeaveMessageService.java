package com.lfj.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lfj.blog.entity.LeaveMessage;
import com.lfj.blog.service.vo.LeaveMessageVo;

import java.util.List;

/**
 * @author 16658
 * @description 针对表【leave_message(留言表)】的数据库操作Service
 * @createDate 2024-04-16 15:01:52
 */
public interface ILeaveMessageService extends IService<LeaveMessage> {
	/**
	 * 新增留言
	 *
	 * @param content
	 */
	void add(String content);

	/**
	 * 留言回复
	 *
	 * @param pid
	 * @param toUserId
	 * @param content
	 */
	void reply(Integer pid, Integer toUserId, String content);

	/**
	 * 分页获取留言及回复列表
	 *
	 * @param current
	 * @param size
	 * @return
	 */
	IPage<LeaveMessageVo> page(long current, long size);

	/**
	 * 删除（本人和管理可删除）
	 *
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 最新留言
	 *
	 * @param limit
	 * @return
	 */
	List<LeaveMessageVo> selectLatest(long limit);
}
