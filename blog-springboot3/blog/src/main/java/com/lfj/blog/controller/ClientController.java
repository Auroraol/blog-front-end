package com.lfj.blog.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfj.blog.common.response.ApiResponseResult;
import com.lfj.blog.entity.Client;
import com.lfj.blog.service.IClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 客户端表 前端控制器
 *
 * @Author: LFJ
 * @Date: 2024-01-25 14:02
 */
@RestController
@RequestMapping("/client")
@Tag(name = "客户端服务", description = "/client")
public class ClientController {

	@Autowired
	private IClientService clientService;

	@GetMapping("/page")
	@PreAuthorize("hasAuthority('admin')")
	@Operation(summary = "分页获取客户端列表", description = "需要accessToken，需要管理员权限")
	public ApiResponseResult<Page<Client>> page(@Parameter(description = "页码") @RequestParam(value = "current", required = false, defaultValue = "1") long current,
												@Parameter(description = "每页数量") @RequestParam(value = "size", required = false, defaultValue = "5") long size) {
		return ApiResponseResult.success(clientService.getUserListWithPagination(current, size));
	}


	@DeleteMapping("/delete/{id}")
	@Operation(summary = "删除客户端", description = "需要accessToken，需要管理员权限")
	public ApiResponseResult<Object> delete(@Parameter(description = "id") @PathVariable(value = "id") int id) {
		if (clientService.removeById(id))
			return ApiResponseResult.success();
		clientService.clearCache(); //清楚Redis中client缓存, 不知道这里有啥用
		return ApiResponseResult.operationError();
	}

	@PostMapping("/save")
	@Operation(summary = "新增或更新客户端,id为null时新增", description = "需要accessToken，需要管理员权限")
	public ApiResponseResult<Object> save(@Validated @RequestBody Client client) {
		clientService.validateExist(client); //id为空时, 检测是否clientId已存在, 不存在则新增, 存在返回客户端已存在
		/**
		 * 	使用Apache Commons Codec库中的Base64类来进行Base64编码
		 * 		String encodedClientId = new String(Base64.encodeBase64(client.getClientId().getBytes()));
		 * 		String encodedClientSecret = new String(Base64.encodeBase64(client.getClientSecret().getBytes()));
		 * 		client.setClientId(encodedClientId);
		 * 		client.setClientSecret(encodedClientSecret);
		 *
		 * 太麻烦了, 选择前端在传值的时候传递加密后的数据
		 */
		clientService.saveOrUpdate(client);
		clientService.clearCache();
		return ApiResponseResult.success();
	}
}
