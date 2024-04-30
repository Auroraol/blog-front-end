package com.lfj.blog.common.validator.annotation;

import com.lfj.blog.common.validator.IsImageValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 是否类型文件类型为图片注解
 * <p>
 * 2019-11-13 11:02
 **/
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsImageValidator.class) // 指定校验的实现类
public @interface IsImage {
	boolean required() default true;

	// 下面的message, groups和payload也是必须添加的
	String message() default "文件格式不正确，只限bmp,gif,jpeg,jpeg,png,webp格式";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
