package com.example.demo.clothes.dto.response;

import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.entity.GenderCategory;

public record GetClothesResponse(Long id, String imageUrl, String name, Integer price, GenderCategory genderCategory,
								 String productNumber, Integer discount) {

	public static GetClothesResponse from(Clothes clothes) {
		return new GetClothesResponse(clothes.getId(), clothes.getImageUrl(), clothes.getName(), clothes.getPrice(),
			clothes.getGenderCategory(), clothes.getProductNumber(), clothes.getDiscount());
	}
}
