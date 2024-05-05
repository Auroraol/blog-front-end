package com.lfj.blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.service.ILeaveMessageService;
import com.lfj.blog.service.vo.LeaveMessageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 留言表 前端控制器
 *
 * @Author: LFJ
 * @Date: 2024-04-04 16:15
 */
@RestController
@RequestMapping("/leave/message")
@Tag(name = "留言服务", description = "/leave/message")
public class LeaveMessageController {


	@Autowired
	private ILeaveMessageService messageService;

	@PostMapping("/add")
	@Operation(summary = "新增留言", description = "需要accessToken")
	public ApiResponseResult add(@Parameter(description = "留言内容") @NotBlank(message = "内容不能为空") @RequestParam("content") String content) {
		messageService.add(content);
		return ApiResponseResult.success();
	}

	@PostMapping("/reply")
	@Operation(summary = "留言回复", description = "需要accessToken")
	public ApiResponseResult reply(
			@Parameter(description = "父id") @NotNull(message = "pid不能为空") @RequestParam(value = "pid") Integer pid,
			@Parameter(description = "被回复者id") @NotNull(message = "被回复者id不能为空") @RequestParam(value = "toUserId") Integer toUserId,
			@Parameter(description = "回复内容") @NotBlank(message = "内容不能为空") @RequestParam("content") String content) {
		messageService.reply(pid, toUserId, content);
		return ApiResponseResult.success();
	}

	@GetMapping("/page")
	@Operation(summary = "分页获取留言及回复列表", description = "包括留言者和回复者信息")
	public ApiResponseResult<IPage<LeaveMessageVo>> page(
			@Parameter(description = "页码") @RequestParam(value = "current", required = false, defaultValue = "1") long current,
			@Parameter(description = "每页数量") @RequestParam(value = "size", required = false, defaultValue = "5") long size) {
		return ApiResponseResult.success(messageService.page(current, size));
	}

	@DeleteMapping("/delete/{id}")
	@Operation(summary = "删除留言或留言回复", description = "需要accessToken，本人和管理员可删除")
	public ApiResponseResult delete(@Parameter(description = "id") @PathVariable("id") Integer id) {
		messageService.delete(id);
		return ApiResponseResult.success();
	}

	@GetMapping("/latest")
	@Operation(summary = "最新留言列表", description = "包括留言者")
	public ApiResponseResult<List<LeaveMessageVo>> latest(
			@Parameter(description = "数量限制,默认值:5") @RequestParam(value = "limit", required = false, defaultValue = "5") long limit) {
		return ApiResponseResult.success(messageService.selectLatest(limit));
	}


}
