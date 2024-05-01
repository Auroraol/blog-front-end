package com.lfj.blog.service.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ArticleArchivesVo对象
 * 文章归档
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "ArticleArchivesVo对象", description = "文章归档")
public class ArticleArchivesVo {

	@ApiModelProperty(value = "年月,格式yyyy-mm")
	private String yearMonth;

	@TableField(value = "article_count")
	@ApiModelProperty(value = "数量")
	private long articleCount;
}
