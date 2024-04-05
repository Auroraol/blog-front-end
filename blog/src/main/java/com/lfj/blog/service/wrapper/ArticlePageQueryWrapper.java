package com.lfj.blog.service.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePageQueryWrapper {

	private Integer status;

	private Long offset;

	private Long limit;

	private Integer categoryId;

	private Integer tagId;

	private String title;

	private String orderBy;

	private String start;

	private String end;
}
