package com.lfj.blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.service.ILeaveMessageService;
import com.lfj.blog.service.vo.LeaveMessageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 留言表 前端控制器
 */
@RestController
@RequestMapping("/leave/message")
@Api(tags = "留言服务", value = "/leave/message")
public class LeaveMessageController {


	@Autowired
	private ILeaveMessageService messageService;

	@PostMapping("/add")
	@ApiOperation(value = "新增留言", notes = "需要accessToken")
	public ApiResponseResult add(@ApiParam("留言内容") @NotBlank(message = "内容不能为空") @RequestParam("content") String content) {
		messageService.add(content);
		return ApiResponseResult.success();
	}

	@PostMapping("/reply")
	@ApiOperation(value = "留言回复", notes = "需要accessToken")
	public ApiResponseResult reply(
			@ApiParam("父id") @NotNull(message = "pid不能为空") @RequestParam(value = "pid") Integer pid,
			@ApiParam("被回复者id") @NotNull(message = "被回复者id不能为空") @RequestParam(value = "toUserId") Integer toUserId,
			@ApiParam("回复内容") @NotBlank(message = "内容不能为空") @RequestParam("content") String content) {
		messageService.reply(pid, toUserId, content);
		return ApiResponseResult.success();
	}

	@GetMapping("/page")
	@ApiOperation(value = "分页获取留言及回复列表", notes = "包括留言者和回复者信息")
	public ApiResponseResult<IPage<LeaveMessageVo>> page(
			@ApiParam("页码") @RequestParam(value = "current", required = false, defaultValue = "1") long current,
			@ApiParam("每页数量") @RequestParam(value = "size", required = false, defaultValue = "5") long size) {
		return ApiResponseResult.success(messageService.page(current, size));
	}

	@DeleteMapping("/delete/{id}")
	@ApiOperation(value = "删除留言或留言回复", notes = "需要accessToken，本人和管理员可删除")
	public ApiResponseResult delete(@ApiParam("id") @PathVariable("id") Integer id) {
		messageService.delete(id);
		return ApiResponseResult.success();
	}

	@GetMapping("/latest")
	@ApiOperation(value = "最新留言列表", notes = "包括留言者")
	public ApiResponseResult<List<LeaveMessageVo>> latest(
			@ApiParam("数量限制,默认值:5") @RequestParam(value = "limit", required = false, defaultValue = "5") long limit) {
		return ApiResponseResult.success(messageService.selectLatest(limit));
	}


}
