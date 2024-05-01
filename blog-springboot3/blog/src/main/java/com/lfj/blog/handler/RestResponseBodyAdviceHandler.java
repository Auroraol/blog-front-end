package com.lfj.blog.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfj.blog.common.response.ApiResponseResult;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * @Author: LFJ
 * @Date: 2024-01-25 11:04
 * 使用Spring提供的ResponseBodyAdvice来实现统一响应格式的封装
 * 重写了其中的supports和beforeBodyWrite方法。
 * 其中，supports方法用于判断当前返回类型是否需要进行处理，beforeBodyWrite方法则用于对返回结果进行封装
 */
@RestControllerAdvice
@Slf4j
public class RestResponseBodyAdviceHandler implements ResponseBodyAdvice<Object> {
	private final String stringConverter = "org.springframework.http.converter.StringHttpMessageConverter";
	@Resource
	private ObjectMapper objectMapper;

	/**
	 * true:代表支持我们在响应前端的时候做一些处理(调用beforeBodyWrite方法)
	 * false:不支持
	 *
	 * @param returnType
	 * @param converterType
	 * @return
	 */
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		log.info("supports:{}", returnType.getDeclaringClass().getName());
		/**
		 * 排除swagger-ui请求返回数据增强
		 */
		return !returnType.getDeclaringClass().getName().contains("springfox");
	}

	@SneakyThrows
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
								  Class<? extends HttpMessageConverter<?>> selectedConverterType,
								  ServerHttpRequest request, ServerHttpResponse response) {

		/**
		 * 当接口返回到类型消息转换器是StringHttpMessageConverter,才需要把它转换成string
		 */
		if (stringConverter.equalsIgnoreCase(selectedConverterType.getName())) {
			HttpHeaders headers = response.getHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			return objectMapper.writeValueAsString(ApiResponseResult.success(body));
		}
		/**
		 *
		 * 如果响应结果已经是DataResult类型，则直接返回
		 */
		if (body instanceof ApiResponseResult) {
			return body;
		}

		return ApiResponseResult.success(body);
	}
}