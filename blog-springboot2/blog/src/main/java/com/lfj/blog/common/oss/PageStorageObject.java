package com.lfj.blog.common.oss;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 文件分页page
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageStorageObject {

	/**
	 * 数据
	 */
	private List<StorageObject> records;

	/**
	 * 下一个标记
	 */
	private String nextMarker;

	/**
	 * 当前标记
	 */
	private String currentMarker;

	/**
	 * 是否加载完成
	 */
	private boolean loadedAll;

	/**
	 * 数量
	 */
	private int size;
}
