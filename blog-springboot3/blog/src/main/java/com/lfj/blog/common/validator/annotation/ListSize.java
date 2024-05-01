package com.lfj.blog.common.validator.annotation;


import com.lfj.blog.common.validator.ListSizeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 集合长度
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
