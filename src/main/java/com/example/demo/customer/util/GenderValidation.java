package com.example.demo.customer.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.customer.entity.Gender;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

public class GenderValidation {

	@Target({ElementType.FIELD, ElementType.PARAMETER})
	@Retention(RetentionPolicy.RUNTIME)
	@Constraint(validatedBy = GenderValidator.class)
	public @interface ValidGender {
		String message() default "성별은 M, F, 남자, 여자 중 하나여야 합니다";
		Class<?>[] groups() default {};
		Class<? extends Payload>[] payload() default {};
	}

	public static class GenderValidator implements ConstraintValidator<ValidGender, Gender> {
		@Override
		public boolean isValid(Gender value, ConstraintValidatorContext context) {
			if (value == null) {
				return false;
			}
			return value == Gender.M || value == Gender.F;
		}
	}

}
