package com.lfj.blog.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.controller.model.request.AddCategoryRequest;
import com.lfj.blog.entity.Category;
import com.lfj.blog.service.ICategoryService;
import com.lfj.blog.utils.buildTreeUtil.TreeVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 目录分类表 前端控制器
 *
 * @Author: LFJ
 * @Date: 2024-04-6 15:02
 */
@RestController
@RequestMapping("/category")
@Tag(name = "分类服务", description = "/category")
public class CategoryController {

	@Autowired
	private ICategoryService categoryService;

	@PostMapping("/add")
	@PreAuthorize("hasAuthority('admin')")
	@Operation(summary = "新增分类", description = "需要accessToken，需要管理员权限")
	public ApiResponseResult add(@Validated @RequestBody AddCategoryRequest request) {
		categoryService.add(request);
		return ApiResponseResult.success();
	}

	@GetMapping("/tree")
	@Operation(summary = "获取分类树", description = "数据结构为树型结构，需要accessToken，需要管理员权限")
	public ApiResponseResult<List<TreeVo<Category>>> tree() {
		return ApiResponseResult.success(categoryService.getCategoryNodeTree());
	}

	@GetMapping("/list")
	@Operation(summary = "获取分类列表", description = "不分上下级，返回所有分类(已删除除外)")
	public ApiResponseResult<List<Category>> list() {
		
		return ApiResponseResult.success(categoryService.list());
	}

	@GetMapping("/page")
	@PreAuthorize("hasAuthority('admin')")
	@Operation(summary = "分页获取分类", description = "需要accessToken，需要管理员权限")
	public ApiResponseResult<IPage<Category>> page(@Parameter(description = "页码") @RequestParam(value = "current", required = false, defaultValue = "1") long current,
												   @Parameter(description = "每页数量") @RequestParam(value = "size", required = false, defaultValue = "5") long size) {
		Page<Category> page = new Page<>(current, size);
		return ApiResponseResult.success(categoryService.page(page));
	}


	@PostMapping("/update")
	@PreAuthorize("hasAuthority('admin')")
	@Operation(summary = "修改分类名", description = "需要accessToken，需要管理员权限")
	public ApiResponseResult update(@Parameter(description = "分类id") @RequestParam("id") int id,
									@Parameter(description = "标签名") @RequestParam(value = "name") String name) {
		categoryService.updateCategoryById(id, name);
		return ApiResponseResult.success();
	}


	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('admin')")
	@Operation(summary = "删除分类", description = "需要accessToken，逻辑删除,需要管理员权限，若存在子类，则不允许删除")
	public ApiResponseResult delete(@Parameter(description = "分类id") @PathVariable("id") int id) {
		System.out.println("ss");
		categoryService.delete(id);
		return ApiResponseResult.success();
	}

}
