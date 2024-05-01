package com.lfj.blog.utils;

import com.alibaba.fastjson2.JSON;
import com.lfj.blog.common.response.ApiResponseResult;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @Author: LFJ
 * @Date: 2024-03-10 23:14
 * response 输出响应工具, 用于任何地方发起响应数据, 不需要PostMapping()等等
 * 实现了在任何地方方便地输出响应数据到HttpServletResponse中的功能。
 */
@Slf4j
public class ResponseUtil {

	static final String ENCODING = "UTF-8";
	static final String CONTENT_TYPE = "application/json;charset=UTF-8";

	/**
	 * 输出前端内容以及状态指定
	 *
	 * @param response
	 * @param status
	 * @param content
	 */
	public static void output(HttpServletResponse response, Integer status, String content) {
		ServletOutputStream servletOutputStream = null;
		try {
			response.setCharacterEncoding(ENCODING);
			response.setContentType(CONTENT_TYPE);
			response.setStatus(status);
			servletOutputStream = response.getOutputStream();
			servletOutputStream.write(content.getBytes());
		} catch (Exception e) {
			log.error("response output error: ", e);
		} finally {
			if (servletOutputStream != null) {
				try {
					servletOutputStream.flush();
					servletOutputStream.close();
				} catch (IOException e) {
					log.error("response output IO close error:", e);
				}
			}
		}
	}


	/**
	 * response 输出JSON
	 *
	 * @param response
	 * @param status   response 状态
	 * @param result
	 */
	public static void output(HttpServletResponse response, Integer status, ApiResponseResult result) {
		response.setStatus(status);
		output(response, result);
	}


	/**
	 * response 输出JSON
	 *
	 * @param response
	 * @param result
	 */
	public static void output(HttpServletResponse response, ApiResponseResult result) {
		ServletOutputStream servletOutputStream = null;
		try {
			response.setCharacterEncoding(ENCODING);
			response.setContentType(CONTENT_TYPE);
			servletOutputStream = response.getOutputStream();
//			System.out.println(result.getMessage());
			servletOutputStream.write(JSON.toJSONString(result).getBytes());
			log.info("Response output successful");
		} catch (Exception e) {
			log.error("response output error:", e);
		} finally {
			if (servletOutputStream != null) {
				try {
					servletOutputStream.flush();
					servletOutputStream.close();
				} catch (IOException e) {
					log.error("response output IO close error:", e);
				}
			}
		}
	}

}
