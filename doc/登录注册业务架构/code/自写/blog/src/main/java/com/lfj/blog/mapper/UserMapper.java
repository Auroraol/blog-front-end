package com.lfj.blog.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lfj.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 16658
 * @description 针对表【user】的数据库操作Mapper
 * @createDate 2024-03-09 17:46:34
 * @Entity com.lfj.blog.entity.User
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {
	int insertSelective(User user);

	List<User> selectAllByUsername(@Param("username") String username);

	// 更新nickname
//	int
//	(@Param("username") String userName, @Param("nickname") String nickName);
	int updateNicknameByUsername(@Param("nickname") String nickname, @Param("username") String username);
}




