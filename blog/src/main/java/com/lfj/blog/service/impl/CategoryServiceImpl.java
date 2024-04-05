package com.lfj.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfj.blog.entity.Category;
import com.lfj.blog.service.CategoryService;
import com.lfj.blog.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 16658
* @description 针对表【category(分类表)】的数据库操作Service实现
* @createDate 2024-04-05 22:34:31
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




