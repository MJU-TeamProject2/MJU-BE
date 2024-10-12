package com.example.demo.clothes.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.clothes.entity.GenderCategory;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

public class GenderCategoryValidation {

	@Target({ElementType.FIELD, ElementType.PARAMETER})
	@Retention(RetentionPolicy.RUNTIME)
	@Constraint(validatedBy = GenderCategoryValidator.class)
	public @interface ValidGenderCategory {
		String message() default "성별 카테고리는 남성용, 여성용, 공용 중 하나여야 합니다";
		Class<?>[] groups() default {};
		Class<? extends Payload>[] payload() default {};
	}

	public static class GenderCategoryValidator implements ConstraintValidator<ValidGenderCategory, GenderCategory> {
		@Override
		public boolean isValid(GenderCategory value, ConstraintValidatorContext context) {
			if (value == null) {
				return false;
			}
			return value == GenderCategory.FEMALE || value == GenderCategory.MALE || value == GenderCategory.UNISEX;
		}
	}
}
