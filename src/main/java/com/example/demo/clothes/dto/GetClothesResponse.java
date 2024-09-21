package com.example.demo.clothes.dto;

import com.example.demo.clothes.entity.Clothes;

public record GetClothesResponse(Long id, String imageUrl, String name, Integer price, String detail, Integer quantity) {

	public static GetClothesResponse from(Clothes clothes) {
		return new GetClothesResponse(clothes.getId(), clothes.getImageUrl(), clothes.getName(), clothes.getPrice(),
			clothes.getDetail(), clothes.getQuantity());
	}
}
