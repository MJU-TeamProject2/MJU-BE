package com.example.demo.clothes.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.clothes.entity.ClothesCategory;


@Component
public class StringToClothesCategoryConverter implements Converter<String, ClothesCategory> {
	@Override
	public ClothesCategory convert(String value) {
		try {
			return ClothesCategory.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid clothes category: " + value);
		}
	}

}
