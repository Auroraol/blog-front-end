package com.lfj.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import com.lfj.blog.controller.model.request.AddCategoryRequest;
import com.lfj.blog.entity.Category;
import com.lfj.blog.exception.ApiException;
import com.lfj.blog.mapper.CategoryMapper;
import com.lfj.blog.service.IArticleService;
import com.lfj.blog.service.ICategoryService;
import com.lfj.blog.utils.buildTreeUtil.BuildTreeUtil;
import com.lfj.blog.utils.buildTreeUtil.TreeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 目录分类表 服务实现类
 *
 * @author 16658
 * @description 针对表【category(分类表)】的数据库操作Service实现
 * @createDate 2024-04-05 22:34:31
 */
@Service
public class ICategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
		implements ICategoryService {
	/**
	 * 根目录父id
	 */
	private final static Integer ROOT_PARENT_ID = 0;

	@Autowired
	private IArticleService articleService;


	/**
	 * 新增分类
	 *
	 * @param request
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)   //开启事务
	public void add(AddCategoryRequest request) {
		Category daoCategory = selectByBName(request.getName());
		if (daoCategory != null) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "分类已存在");
		}
		Category category = new Category();
		category.setName(request.getName());
		category.setParentId(request.getParentId());
		this.save(category);
	}

	/**
	 * 分类目录树
	 *
	 * @return
	 */
	@Override
	public List<TreeVo<Category>> getCategoryNodeTree() {
		// 1. 查询所有数据库的数据
		List<Category> list = this.list();
		// 2.将 Category 类型的列表转换为 TreeVo<Category> 类型的列表
		List<TreeVo<Category>> treeNodes = convertToTreeNodes(list);
		// 3. 使用工具类
		return BuildTreeUtil.buildList(treeNodes, "0");
	}


	/**
	 * 修改
	 *
	 * @param id
	 * @param name
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateCategoryById(int id, String name) {
		Category daoCategory = selectByBName(name);
		if (daoCategory != null && daoCategory.getName().equals(name)) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "分类已存在");
		}
		Category category = new Category();
		category.setId(id);
		category.setName(name);
		updateById(category);
		// 文章根系目录名
//		articleService.updateCategoryName(id, name);
	}

	/**
	 * 删除分类
	 *
	 * @param id
	 */

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(int id) {
		Category daoCategory = this.getById(id);
		if (daoCategory == null) {
			return;
		}
		QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(Category::getParentId, id);
		Category childrenCategory = getOne(queryWrapper, false);
		// 有子类
		if (childrenCategory != null) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "该分类存在子类,不允许删除");
		}
		this.removeById(id);
	}

	/**
	 * 获取子元素对应父元素列表，顺序为 root node2 node3
	 *
	 * @param categoryId 当前类别id
	 * @return
	 */
	@Override
	public List<Category> parentList(Integer categoryId) {
		List<Category> daoCategoryList = list();
		Category currentCategory = null;
		Map<Integer, Category> map = new HashMap<>(daoCategoryList.size());
		for (Category category : daoCategoryList) {
			if (categoryId.equals(category.getId())) {
				currentCategory = category;
			}
			map.put(category.getId(), category);
		}
		List<Category> list = new ArrayList<>();
		List<Category> categoryList = addParent(currentCategory, list, map);
		Collections.reverse(categoryList);
		return categoryList;

	}

	/**
	 * 根据子元素递归获取父元素列表
	 *
	 * @param category
	 * @return
	 */
	private List<Category> addParent(Category category, List<Category> parentList, Map<Integer, Category> map) {
		if (category == null) {
			return parentList;
		}
		parentList.add(category);
		Category parent = map.get(category.getParentId());
		return addParent(parent, parentList, map);
	}

	/**
	 * 根据名称查询
	 *
	 * @param name
	 * @return
	 */
	private Category selectByBName(String name) {
		QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(Category::getName, name);
		return this.getOne(queryWrapper, false);
	}

	/**
	 * 列表转换, xxx-->TreeVo<xxx>
	 *
	 * @param list
	 * @return
	 */
	private List<TreeVo<Category>> convertToTreeNodes(List<Category> list) {
		List<TreeVo<Category>> treeNodes = new ArrayList<>();
		for (Category category : list) {
			TreeVo<Category> treeNode = new TreeVo<>();
			// 修改
			treeNode.setId(String.valueOf(category.getId()));
			treeNode.setParentId(String.valueOf(category.getParentId()));
			treeNode.setText(category.getName());
			treeNodes.add(treeNode);
		}
		return treeNodes;
	}
}




