package com.example.demo.clothes.dto.response;

import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.entity.GenderCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "Clothes Response", description = "의류 정보 조회 응답")
public record GetClothesResponse(@Schema(description = "의류 id")
								 Long id,
								 @Schema(description = "의류 이미지")
								 String imageUrl,
								 @Schema(description = "의류 이름")
								 String name,
								 @Schema(description = "의류 가격")
								 Integer price,
								 @Schema(description = "의류 성별 분류")
								 GenderCategory genderCategory,
								 @Schema(description = "의류 품번")
								 String productNumber,
								 @Schema(description = "의류 할인률")
								 Integer discount) {

	public static GetClothesResponse of(Clothes clothes, String imageUrl) {
		return GetClothesResponse.builder()
			.id(clothes.getId())
			.imageUrl(imageUrl)
			.name(clothes.getName())
			.price(clothes.getPrice())
			.genderCategory(clothes.getGenderCategory())
			.productNumber(clothes.getProductNumber())
			.discount(clothes.getDiscount())
			.build();
	}
}
