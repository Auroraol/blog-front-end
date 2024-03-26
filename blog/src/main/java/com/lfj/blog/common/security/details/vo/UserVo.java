package com.lfj.blog.common.security.details.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.lfj.blog.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 对User二次封装, 用于Security授权功能
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "UserVo对象", description = "用户详细信息")
public class UserVo extends User {

	/**
	 * 角色列表
	 */
	@ApiModelProperty(value = "角色列表")
	protected List<String> roles;            // 存储当前权限信息

}
