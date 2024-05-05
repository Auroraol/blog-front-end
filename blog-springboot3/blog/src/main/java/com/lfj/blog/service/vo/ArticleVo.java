package com.lfj.blog.service.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.lfj.blog.entity.Article;
import com.lfj.blog.entity.Category;
import com.lfj.blog.entity.Tag;
import com.lfj.blog.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * ArticleVo对象
 * 文章返回前端对象
 * 文章详细对象
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "ArticleVo对象", description = "文章详细对象")
public class ArticleVo extends Article {

	@Schema(description = "作者信息")
	private User user;

	@Schema(description = "标签列表")
	private List<Tag> tagList;

	@Schema(description = "分类列表,顺序:root node2 node3")
	private List<Category> categoryList;

	@Schema(description = "上一篇")
	private Article previous;

	@Schema(description = "下一篇")
	private Article next;

	@Schema(description = "推荐分数")
	private Double recommendScore;

}
