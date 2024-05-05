package com.lfj.blog.controller.model.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lfj.blog.common.validator.annotation.ListSize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 文章请求json
 *
 * @Author: LFJ
 * @Date: 2024-04-6 15:02
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "文章请求json", description = "文章请求体")
public class ArticleRequest {

	@Schema(description = "文章id，编辑时不可空")
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	@NotNull(message = "是否原创标识不能为空")
	@Schema(description = "是否原创，1:是，0:否")
	private Integer original;

	@NotNull(message = "状态不能为空")
	@Schema(description = "状态，0:发布，1:保存")
	private Integer status;

	@NotNull(message = "文章分类id不能为空")
	@Schema(description = "文章分类id")
	private Integer categoryId;

	@NotBlank(message = "文章标题不能为空")
	@Schema(description = "文章标题")
	private String title;

	@NotBlank(message = "文章摘要不能为空")
	@Schema(description = "文章摘要")
	private String summary;

	@NotBlank(message = "文章内容不能为空")
	@Schema(description = "文章内容")
	private String content;

	@Schema(description = "文章内容")
	private String htmlContent;

	@NotBlank(message = "文章封面不能为空")
	@Schema(description = "文章封面")
	private String cover;

	@ListSize(max = 4, message = "一篇文章最多只允许添加4个标签")
	@Schema(description = "文章标签id列表")
	private List<Integer> tagIds;

	@Schema(description = "转载地址，转载非空")
	private String reproduce;

}
