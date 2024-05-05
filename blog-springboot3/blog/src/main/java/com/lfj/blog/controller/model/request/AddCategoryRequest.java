package com.lfj.blog.controller.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 添加分类json
 *
 * @Author: LFJ
 * @Date: 2024-04-6 15:02
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "添加分类json", description = "添加分类")
public class AddCategoryRequest {

	@NotBlank(message = "分类名称不能为空")
	@Schema(description = "名称")
	private String name;

	@NotNull(message = "parentId不能为空")
	@Schema(description = "父类id,添加根目录分类时值为0")
	private Integer parentId;
}
