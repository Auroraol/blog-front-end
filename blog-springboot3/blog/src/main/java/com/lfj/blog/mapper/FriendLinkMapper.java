package com.lfj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lfj.blog.entity.FriendLink;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 16658
 * @description 针对表【friend_link(友链表)】的数据库操作Mapper
 * @createDate 2024-04-03 16:18:02
 * @Entity com.lfj.blog.entity.FriendLink
 */

@Mapper
public interface FriendLinkMapper extends BaseMapper<FriendLink> {

}




