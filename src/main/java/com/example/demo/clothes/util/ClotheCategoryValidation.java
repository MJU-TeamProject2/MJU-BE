package com.example.demo.clothes.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.clothes.entity.ClothesCategory;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

public class ClotheCategoryValidation {

	@Target({ElementType.FIELD, ElementType.PARAMETER})
	@Retention(RetentionPolicy.RUNTIME)
	@Constraint(validatedBy = ClotheCategoryValidator.class)
	public @interface ValidClotheCategory {
		String message() default "성별 카테고리는 남성용, 여성용, 공용 중 하나여야 합니다";
		Class<?>[] groups() default {};
		Class<? extends Payload>[] payload() default {};
	}

	public static class ClotheCategoryValidator implements ConstraintValidator<ValidClotheCategory, ClothesCategory> {
		@Override
		public boolean isValid(ClothesCategory value, ConstraintValidatorContext context) {
			if (value == null) {
				return false;
			}
			return value == ClothesCategory.TOPS || value == ClothesCategory.DRESSES
				|| value == ClothesCategory.OUTERWEAR || value == ClothesCategory.PANTS
				|| value == ClothesCategory.SHOES;
		}
	}
}
