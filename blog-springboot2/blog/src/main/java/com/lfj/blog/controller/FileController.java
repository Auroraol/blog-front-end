package com.lfj.blog.controller;

import com.lfj.blog.common.oss.PageStorageObject;
import com.lfj.blog.common.oss.Storage;
import com.lfj.blog.common.response.ApiResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * @Author: LFJ
 * @Date: 2024-04-5 12:42
 **/
@RestController
@RequestMapping("/file")
@Log4j2
@Api(tags = "文件存储服务", value = "file")
public class FileController {

	@Autowired
	private Storage storage;

	@PostMapping("/upload")
	@PreAuthorize("hasAuthority('admin')")
	@ApiOperation(value = "上传文件", notes = "需要accessToken，需要管理员权限")
	public ApiResponseResult<String> upload(@NotNull @RequestParam("file") MultipartFile file) throws IOException {
		String filename = file.getOriginalFilename();
		String contentType = file.getContentType();
		String extension = filename.substring(filename.lastIndexOf(".") + 1);
		String name = System.currentTimeMillis() + "." + extension; // 生成唯一文件名
		String fullPath = storage.upload(file.getInputStream(), name, contentType);
		return ApiResponseResult.success(fullPath);
	}

	@DeleteMapping("/delete")
	@PreAuthorize("hasAuthority('admin')")
	@ApiOperation(value = "删除文件", notes = "需要accessToken，需要管理员权限")
	public ApiResponseResult delete(@ApiParam("文件全路径") @NotNull @RequestParam("fullPath") String fullPath) {
		storage.delete(fullPath);
		return ApiResponseResult.success();
	}

	@GetMapping("/page")
	@PreAuthorize("hasAuthority('admin')")
	@ApiOperation(value = "分页分类列表", notes = "需要accessToken，需要管理员权限")
	public ApiResponseResult<PageStorageObject> page(@ApiParam("marker标记") @RequestParam(value = "marker", required = false) String marker,
													 @ApiParam("每页数量") @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
		return ApiResponseResult.success(storage.page(marker, size));
	}
}
