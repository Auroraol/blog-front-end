package com.lfj.blog.common.validator.annotation;

import com.lfj.blog.common.validator.YearMonthFormatValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * yyyy-mm,年月格式校验
 **/
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = YearMonthFormatValidator.class)
public @interface YearMonthFormat {
	boolean required() default true;

	String message() default "年月格式不正确";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
