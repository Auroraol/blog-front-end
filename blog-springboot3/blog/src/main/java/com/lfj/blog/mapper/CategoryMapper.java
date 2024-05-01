package com.lfj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lfj.blog.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 16658
 * @description 针对表【category(分类表)】的数据库操作Mapper
 * @createDate 2024-04-05 22:34:31
 * @Entity com.lfj.blog.entity.Category
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




