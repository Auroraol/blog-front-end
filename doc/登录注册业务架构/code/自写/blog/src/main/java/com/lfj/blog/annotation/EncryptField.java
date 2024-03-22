package com.lfj.blog.annotation;

import java.lang.annotation.*;

/**
 * 用于标记需要加密的字段或参数
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptField {

	String[] value() default "";
}


