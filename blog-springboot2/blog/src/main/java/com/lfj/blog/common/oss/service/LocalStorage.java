package com.lfj.blog.common.oss.service;


import com.lfj.blog.common.oss.AbstractStorage;
import com.lfj.blog.common.oss.PageStorageObject;
import com.lfj.blog.common.oss.config.StorageProperties;
import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import com.lfj.blog.exception.ApiException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 本地 存储
 *
 * @author: yaohw
 * @create: 2019-10-31 19:50
 **/
@Log4j2
public class LocalStorage extends AbstractStorage {

	private String path;  //  文件保存根路径
	private String proxy; // 公网url
	private String virtualPath;   // 虚拟路径, 这个虚拟路径需要于文件保存根路径映射

	public LocalStorage(StorageProperties.Local local) {
		this.path = local.getPath();
		this.proxy = local.getProxy();
		this.virtualPath = local.getVirtualPath();
		init();
	}

	private void init() {
		File file = new File(path);
		try {
			FileUtils.forceMkdir(file);
		} catch (IOException ex) {
			log.error("创建本地存储文件目录失败:{0}", ex);
		}
	}

	/**
	 * 文件上传
	 *
	 * @param bytes       件字节数组
	 * @param name        文件路径
	 * @param contentType 文件类型
	 * @return http地址
	 */
	@Override
	public String upload(byte[] bytes, String name, String contentType) {
		return upload(new ByteArrayInputStream(bytes), name, contentType);
	}

	/**
	 * 文件上传
	 *
	 * @param inputStream 字节流
	 * @param name        文件名
	 * @param contentType 文件类型
	 * @return http地址
	 */
	@Override
	public String upload(InputStream inputStream, String name, String contentType) {
		File file = new File(path + name);
		try {
			FileUtils.copyInputStreamToFile(inputStream, file);
		} catch (IOException ex) {
			log.error("本地文件存储失败:{0}", ex);
			throw new ApiException(ResponseCodeEnum.SYSTEM_ERROR.getCode(), "上传文件失败");
		}
		return proxy + virtualPath + name;
	}

	/**
	 * 删除文件
	 *
	 * @param fullPath 文件完整路径
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(String fullPath) {
		if (StringUtils.isBlank(fullPath)) {
			return false;
		}
		return FileUtils.deleteQuietly(new File(path + getFileNameFromFullPath(fullPath)));
	}

	/**
	 * 分页获取文件对象列表
	 *
	 * @param nextMarker 下一个marker
	 * @param size
	 * @return
	 */
	@Override
	public PageStorageObject page(String nextMarker, int size) {
		return new PageStorageObject();
	}
}
