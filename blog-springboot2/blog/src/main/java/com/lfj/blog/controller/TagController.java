package com.lfj.blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.entity.Tag;
import com.lfj.blog.service.ITagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 标签表 前端控制器
 *
 * @Author: LFJ
 * @Date: 2024-04-04 10:02
 */
@RestController
@RequestMapping("/tag")
@Api(tags = "标签服务", value = "/tag")
public class TagController {

	@Autowired
	private ITagService tagService;

	@PostMapping("/add")
	@PreAuthorize("hasAuthority('admin')")  // 需要的权限
	@ApiOperation(value = "添加标签", notes = "需要accessToken，需要管理员权限")
	public ApiResponseResult addTag(@NotBlank(message = "标签名不能为空") @RequestParam("tagName") String tagName) {
		tagService.addTag(tagName);
		return ApiResponseResult.success();
	}
	
	@GetMapping("/id")
	@ApiOperation(value = "查询标签id")
	public ApiResponseResult getTagId(@NotBlank(message = "标签名不能为空") @RequestParam("tagName") String tagName) {
		return ApiResponseResult.success(tagService.selectIdByName(tagName));
	}


	@GetMapping("/page")
	@PreAuthorize("hasAuthority('admin')")
	@ApiOperation(value = "分页查询标签", notes = "需要accessToken，需要管理员权限")
	public ApiResponseResult<IPage<Tag>> page(@ApiParam("当前页") @RequestParam(value = "current", required = false, defaultValue = "1") long current,
											  @ApiParam("每页数量") @RequestParam(value = "size", required = false, defaultValue = "5") long size,
											  @ApiParam("标签名关键字，可空") @RequestParam(value = "tagName", required = false) String tagName) {
		return ApiResponseResult.success(tagService.selectTagPage(current, size, tagName));
	}


	@GetMapping("/list")
	@ApiOperation(value = "获取标签列表", notes = "不需要accessToken")
	public ApiResponseResult<List<Tag>> list(@ApiParam("标签名关键字，可空") @RequestParam(value = "tagName", required = false) String tagName) {
		return ApiResponseResult.success(tagService.selectTagList(tagName));
	}


	@PostMapping("/update")
	@PreAuthorize("hasAuthority('admin')")
	@ApiOperation(value = "修改标签名", notes = "需要accessToken，需要管理员权限")
	public ApiResponseResult update(@ApiParam("标签id") @RequestParam("id") int id,
									@ApiParam("标签名") @RequestParam(value = "name") String name) {
		tagService.update(id, name);
		return ApiResponseResult.success();
	}


	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('admin')")
	@ApiOperation(value = "删除标签", notes = "需要accessToken,需要管理员权限，逻辑删除")
	public ApiResponseResult delete(@ApiParam("标签id") @PathVariable("id") int id) {
		tagService.delete(id);
		return ApiResponseResult.success();
	}
}
