package com.lfj.blog.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lfj.blog.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 16658
 * @description 针对表【tag(标签表)】的数据库操作Mapper
 * @createDate 2024-04-04 10:38:49
 * @Entity com.lfj.blog.entity.Tag
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {
	List<Tag> selectIdByName(@Param("name") String name);
}




