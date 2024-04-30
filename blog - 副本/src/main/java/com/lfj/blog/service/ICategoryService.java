package com.lfj.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lfj.blog.controller.model.request.AddCategoryRequest;
import com.lfj.blog.entity.Category;
import com.lfj.blog.utils.buildTreeUtil.TreeVo;

import java.util.List;

/**
 * 目录分类表 服务类
 *
 * @author 16658
 * @description 针对表【category(分类表)】的数据库操作Service
 * @createDate 2024-04-05 22:34:31
 */
public interface ICategoryService extends IService<Category> {
	/**
	 * 新增分类
	 *
	 * @param request
	 */
	void add(AddCategoryRequest request);

	/**
	 * 分类目录树
	 *
	 * @return
	 */
	List<TreeVo<Category>> getCategoryNodeTree();

	/**
	 * 修改
	 *
	 * @param id
	 * @param name
	 */
	void updateCategoryById(int id, String name);

	/**
	 * 删除分类
	 *
	 * @param id
	 */
	void delete(int id);

	/**
	 * 获取子元素对应父元素列表，顺序为 node3 node2 root
	 *
	 * @param categoryId
	 * @return
	 */
	List<Category> parentList(Integer categoryId);
}
