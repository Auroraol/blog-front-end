package com.lfj.blog.service.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lfj.blog.entity.Tag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 文章标签计数
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "ArticleTagStatisticsVo对象", description = "文章标签计数")
public class ArticleTagStatisticsVo extends Tag {

	@TableField(value = "article_count")
	@ApiModelProperty("分类文章数量")
	private int articleCount;
}
