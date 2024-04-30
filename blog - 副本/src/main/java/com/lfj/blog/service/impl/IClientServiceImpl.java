package com.lfj.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import com.lfj.blog.entity.Client;
import com.lfj.blog.exception.ApiException;
import com.lfj.blog.mapper.ClientMapper;
import com.lfj.blog.service.IClientService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author 16658
 * @description 针对表【client(客户端表)】的数据库操作Service实现
 * @createDate 2024-03-21 20:46:08
 */
@Service
@Log4j2
public class IClientServiceImpl extends ServiceImpl<ClientMapper, Client>
		implements IClientService {

	@Autowired
	ClientMapper clientMapper;

	/**
	 * 根据客户端id获取客户端
	 *
	 * @param clientId
	 * @return
	 */
	@Override
	@Cacheable(value = "client", key = "#clientId")
	public Client getClientByClientId(String clientId) {
		QueryWrapper<Client> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(Client::getClientId, clientId);
		return getOne(queryWrapper, false);
	}

	/**
	 * 清空缓存
	 */
	@Override
	@CacheEvict(value = "client", allEntries = true)
	public void clearCache() {
		log.info("清空client缓存");
	}


	/**
	 * id为空时, 检测是否clientId已存在, 不存在则新增, 存在返回客户端已存在
	 *
	 * @param client
	 */
	public void validateExist(Client client) {
		if (client.getId() == null) {
			QueryWrapper<Client> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().eq(Client::getClientId, client.getClientId());
			int count = (int) this.count(queryWrapper);
			log.info(count);
			if (count != 0) {
				// 抛出自定义异常 无效请求
				throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "客户端已存在");
			}
		}
	}

	/**
	 * 分页查询
	 *
	 * @param current
	 * @param size
	 * @return
	 */
	@Override
	public Page<Client> getUserListWithPagination(long current, long size) {
		// 创建分页对象
		Page<Client> page = new Page<>(current, size);

		// 第一个参数是分页对象，第二个参数是查询条件（可以是 QueryWrapper 等）
		// selectPage 方法会在查询时自动添加分页条件，并返回分页查询结果
		return clientMapper.selectPage(page, null);
	}
}




