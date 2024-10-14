package com.example.demo.clothes.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.clothes.entity.Size;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

public class SizeValidation {

	@Target({ElementType.FIELD, ElementType.PARAMETER})
	@Retention(RetentionPolicy.RUNTIME)
	@Constraint(validatedBy = SizeValidator.class)
	public @interface ValidSize {
		String message() default "사이즈는 S, M, L, XL, XXL 중 하나여야 합니다";
		Class<?>[] groups() default {};
		Class<? extends Payload>[] payload() default {};
	}

	public static class SizeValidator implements ConstraintValidator<SizeValidation.ValidSize, Size> {
		@Override
		public boolean isValid(Size value, ConstraintValidatorContext context) {
			if (value == null) {
				return false;
			}
			return value == Size.S || value == Size.M || value == Size.L || value == Size.XL || value == Size.XXL;
		}
	}
}
