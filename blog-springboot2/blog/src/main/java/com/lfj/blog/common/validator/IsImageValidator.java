package com.lfj.blog.common.validator;

import com.lfj.blog.common.validator.annotation.IsImage;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * ConstraintValidator<自定义注解类, 校验的数据类型>
 **/
public class IsImageValidator implements ConstraintValidator<IsImage, MultipartFile> {

	private static final String[] IMAGE_CONTENT_TYPE_ARRAY = {"image/bmp", "image/gif", "image/jpeg", "image/png", "image/jpg", "image/webp"};
	private boolean required;

	//自定义的校验逻辑 isValid(校验的数据, ConstraintValidatorContext context)
	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		if (required) {
			String contentType = value.getContentType();
			List<String> imageContentTypeList = Arrays.asList(IMAGE_CONTENT_TYPE_ARRAY);
			return imageContentTypeList.contains(contentType);
		}
		return true;
	}

	//初始化方法
	@Override
	public void initialize(IsImage constraintAnnotation) {
		required = constraintAnnotation.required();
	}
}
