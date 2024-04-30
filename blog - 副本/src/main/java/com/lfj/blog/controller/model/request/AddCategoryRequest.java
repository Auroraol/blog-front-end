package com.lfj.blog.controller.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加分类json
 *
 * @Author: LFJ
 * @Date: 2024-04-6 15:02
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "添加分类json", description = "添加分类")
public class AddCategoryRequest {

	@NotBlank(message = "分类名称不能为空")
	@ApiModelProperty(value = "名称")
	private String name;

	@NotNull(message = "parentId不能为空")
	@ApiModelProperty(value = "父类id,添加根目录分类时值为0")
	private Integer parentId;
}
