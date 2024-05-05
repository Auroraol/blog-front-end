package com.lfj.blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.entity.Tag;
import com.lfj.blog.service.ITagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签表 前端控制器
 *
 * @Author: LFJ
 * @Date: 2024-04-04 10:02
 */
@RestController
@RequestMapping("/tag")
@io.swagger.v3.oas.annotations.tags.Tag(name = "标签服务", description = "/tag")
public class TagController {

	@Autowired
	private ITagService tagService;

	@PostMapping("/add")
	@PreAuthorize("hasAuthority('admin')")  // 需要的权限
	@Operation(summary = "添加标签", description = "需要accessToken，需要管理员权限")
	public ApiResponseResult addTag(@Parameter(description = "标签名") @NotBlank(message = "标签名不能为空") @RequestParam("tagName") String tagName) {
		tagService.addTag(tagName);
		return ApiResponseResult.success();
	}

	@GetMapping("/id")
	@Operation(summary = "查询标签id")
	public ApiResponseResult getTagId(@Parameter(description = "标签id") @NotBlank(message = "标签名不能为空") @RequestParam("tagName") String tagName) {
		return ApiResponseResult.success(tagService.selectIdByName(tagName));
	}


	@GetMapping("/page")
	@PreAuthorize("hasAuthority('admin')")
	@Operation(summary = "分页查询标签", description = "需要accessToken，需要管理员权限")
	public ApiResponseResult<IPage<Tag>> page(@Parameter(description = "当前页") @RequestParam(value = "current", required = false, defaultValue = "1") long current,
											  @Parameter(description = "每页数量") @RequestParam(value = "size", required = false, defaultValue = "5") long size,
											  @Parameter(description = "标签名关键字，可空") @RequestParam(value = "tagName", required = false) String tagName) {
		return ApiResponseResult.success(tagService.selectTagPage(current, size, tagName));
	}


	@GetMapping("/list")
	@Operation(summary = "获取标签列表", description = "不需要accessToken")
	public ApiResponseResult<List<Tag>> list(@Parameter(description = "标签名关键字，可空") @RequestParam(value = "tagName", required = false) String tagName) {
		return ApiResponseResult.success(tagService.selectTagList(tagName));
	}


	@PostMapping("/update")
	@PreAuthorize("hasAuthority('admin')")
	@Operation(summary = "修改标签名", description = "需要accessToken，需要管理员权限")
	public ApiResponseResult update(@Parameter(description = "标签id") @RequestParam("id") int id,
									@Parameter(description = "标签名") @RequestParam(value = "name") String name) {
		tagService.update(id, name);
		return ApiResponseResult.success();
	}


	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('admin')")
	@Operation(summary = "删除标签", description = "需要accessToken,需要管理员权限，逻辑删除")
	public ApiResponseResult delete(@Parameter(description = "标签id") @PathVariable("id") int id) {
		tagService.delete(id);
		return ApiResponseResult.success();
	}
}
