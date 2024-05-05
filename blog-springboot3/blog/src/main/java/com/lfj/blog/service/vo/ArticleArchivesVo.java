package com.lfj.blog.service.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * ArticleArchivesVo对象
 * 文章归档
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "ArticleArchivesVo对象", description = "文章归档")
public class ArticleArchivesVo {

	@Schema(description = "年月,格式yyyy-mm")
	private String yearMonth;

	@TableField(value = "article_count")
	@Schema(description = "数量")
	private long articleCount;
}
