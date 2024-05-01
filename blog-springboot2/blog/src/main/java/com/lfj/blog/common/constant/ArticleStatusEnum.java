package com.lfj.blog.common.constant;

/**
 * 文章状态常量
 **/
public enum ArticleStatusEnum {
	/**
	 * 正常
	 */
	NORMAL(0, "正常"),
	/**
	 * 待发布
	 */
	NOT_PUBLISH(1, "待发布"),
	/**
	 * 丢弃（回收站）
	 */
	DISCARD(2, "丢弃（回收站）");

	private Integer status;

	private String message;

	ArticleStatusEnum(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
