package com.lfj.blog.common.validator.annotation;

/**
 * 2019-11-15 15:23
 **/

import com.lfj.blog.common.validator.ListSizeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 集合长度
 *
 *
 *  2019-11-13 11:02
 **/
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ListSizeValidator.class)
public @interface ListSize {
	boolean required() default true;

	int min() default -1;

	int max() default -1;

	String message();

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
