package com.lfj.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lfj.blog.entity.Client;

/**
 * @author 16658
 * @description 针对表【client(客户端表)】的数据库操作Service
 * @createDate 2024-03-21 20:46:08
 */
public interface IClientService extends IService<Client> {
	/**
	 * 根据客户端id获取客户端
	 *
	 * @param clientId
	 * @return
	 */
	Client getClientByClientId(String clientId);

	/**
	 * 清空缓存
	 */
	void clearCache();

	/**
	 * 校验是否已存在
	 *
	 * @param client
	 */
	void validateExist(Client client);

	/**
	 * 分析查询
	 *
	 * @param current
	 * @param size
	 * @return
	 */
	Page<Client> getUserListWithPagination(long current, long size);
}
