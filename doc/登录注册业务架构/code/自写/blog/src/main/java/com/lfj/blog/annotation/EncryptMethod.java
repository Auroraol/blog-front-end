package com.lfj.blog.annotation;

import java.lang.annotation.*;
import static com.lfj.blog.enums.EncryptConstant.ENCRYPT;

/**
 * 加密方法注解
 */

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptMethod {
	String type() default ENCRYPT;
}

