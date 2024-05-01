package com.lfj.blog.common.oss;

public abstract class AbstractStorage implements Storage {

	/**
	 * 获取文件名 如 1134664743.png
	 *
	 * @param fullPath 完整路径。如 http://qiniu.poile.cn/1134664743.png
	 * @return 文件名
	 */
	protected String getFileNameFromFullPath(String fullPath) {
		if (!fullPath.isEmpty()) {

			return fullPath.substring(fullPath.lastIndexOf("/") + 1);
		}
		return null;
	}
}
