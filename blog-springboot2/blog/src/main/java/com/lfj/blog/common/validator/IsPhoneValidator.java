package com.lfj.blog.common.validator;

import com.lfj.blog.common.validator.annotation.IsPhone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 *
 */
public class IsPhoneValidator implements ConstraintValidator<IsPhone, Object> {

	private boolean required;

	@Override
	public void initialize(IsPhone ca) {
		required = ca.required();
	}

	@Override
	public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
		String mobile = "" + o;
		if (!"".equals(mobile) && required) {
			String regexp = "^[1][3,4,5,6,7,8,9]\\d{9}$";
			return Pattern.matches(regexp, mobile);
		}
		return true;
	}
}
