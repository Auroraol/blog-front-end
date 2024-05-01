package com.lfj.blog.service.security.biz;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * 通用异步服务
 * 使用: asyncService.runAsync((r) -> 回调函数);
 * 用来邮箱发送
 **/
@Log4j2
@Component
public class AsyncService {

	@Async
	public void runAsync(Function<Boolean, Boolean> function) {
		try {
			function.apply(Boolean.TRUE);
		} catch (Exception e) {
			log.error("异步任务执行错误:{0}", e);
		}
	}

}
