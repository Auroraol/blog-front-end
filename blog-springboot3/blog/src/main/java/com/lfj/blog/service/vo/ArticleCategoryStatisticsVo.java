package com.lfj.blog.service.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lfj.blog.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 文章分类统计
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "ArticleCategoriesVo对象", description = "文章分类计数")
public class ArticleCategoryStatisticsVo extends Category {

	@TableField(value = "article_count")
	@Schema(description = "分类文章数量")
	private int articleCount;
}
