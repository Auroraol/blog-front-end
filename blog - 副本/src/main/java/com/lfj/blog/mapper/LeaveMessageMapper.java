package com.lfj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lfj.blog.entity.LeaveMessage;
import com.lfj.blog.service.vo.LeaveMessageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 16658
 * @description 针对表【leave_message(留言表)】的数据库操作Mapper
 * @createDate 2024-04-16 15:01:52
 * @Entity com.lfj.blog.entity.LeaveMessage
 */

@Mapper
public interface LeaveMessageMapper extends BaseMapper<LeaveMessage> {
	/**
	 * 查询留言及留言回复列表，包括留言者和回复者信息
	 *
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<LeaveMessageVo> selectLeaveMessageAndReplyList(@Param("offset") long offset, @Param("limit") long limit);

	/**
	 * 最新留言
	 *
	 * @param limit
	 * @return
	 */
	List<LeaveMessageVo> selectLatest(@Param("limit") long limit);
}




