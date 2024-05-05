package com.lfj.blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfj.blog.common.oss.Storage;
import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.entity.FriendLink;
import com.lfj.blog.service.IFriendLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 友链表 前端控制器
 *
 * @Author: LFJ
 * @Date: 2024-04-03 16:15
 */
@RestController
@RequestMapping("/friend/link")
@Tag(name = "友链服务", description = "/friend/link")
public class FriendLinkController {
	@Autowired
	private IFriendLinkService friendLinkService;

	@Autowired
	private Storage storage;

	@PostMapping("/save")
	@Operation(summary = "新增或更新友链", description = "id不为空时更新，需要accessToken，需要管理员权限")
	public ApiResponseResult add(@RequestBody FriendLink friendLink) {
		friendLinkService.saveOrUpdate(friendLink);
		return ApiResponseResult.success();
	}

	@GetMapping("/list")
	@Operation(summary = "获取友链列表")
	public ApiResponseResult<List<FriendLink>> list() {
		return ApiResponseResult.success(friendLinkService.list());
	}

	@GetMapping("/page")
	@Operation(summary = "分页获取友链列表")
	public ApiResponseResult<IPage<FriendLink>> page(
			@Parameter(description = "页码") @RequestParam(value = "current", required = false, defaultValue = "1") long current,
			@Parameter(description = "每页数量") @RequestParam(value = "size", required = false, defaultValue = "5") long size) {
		Page<FriendLink> page = new Page<>(current, size);
		return ApiResponseResult.success(friendLinkService.page(page));
	}

	@DeleteMapping("/delete/{id}")
	@Operation(summary = "删除友链")
	public ApiResponseResult delete(@Parameter(description = "友链id") @PathVariable(value = "id") int id) {
		FriendLink friendLink = friendLinkService.getById(id);
		if (friendLink != null && StringUtils.isNotBlank(friendLink.getIcon())) {
			deleteIcon(friendLink.getIcon());
		}
		friendLinkService.removeById(id);
		return ApiResponseResult.success();
	}

	/**
	 * 删除icon
	 *
	 * @param path
	 */
	private void deleteIcon(String path) {
		storage.delete(path);
	}
}
