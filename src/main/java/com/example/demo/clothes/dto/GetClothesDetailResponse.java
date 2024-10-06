package com.example.demo.clothes.dto;

import java.util.List;

import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.entity.ClothesCategory;
import com.example.demo.clothes.entity.GenderCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "Clothes Detail Response", description = "특정 의류 상세 정보 조회 응답")
public record GetClothesDetailResponse(@Schema(description = "의류 id", requiredMode = Schema.RequiredMode.REQUIRED)
									   Long clothesId,
									   @Schema(description = "의류 카테고리", requiredMode = Schema.RequiredMode.REQUIRED)
									   ClothesCategory category,
									   @Schema(description = "의류 이미지", requiredMode = Schema.RequiredMode.REQUIRED)
									   String imageUrl,
									   @Schema(description = "의류 이름", requiredMode = Schema.RequiredMode.REQUIRED)
									   String name,
									   @Schema(description = "의류 가격", requiredMode = Schema.RequiredMode.REQUIRED)
									   Integer price,
									   @Schema(description = "의류 성별 분류", requiredMode = Schema.RequiredMode.REQUIRED)
									   GenderCategory genderCategory,
									   @Schema(description = "의류 품번", requiredMode = Schema.RequiredMode.REQUIRED)
									   String productNumber,
									   @Schema(description = "의류 할인률", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
									   Integer discount,
									   @Schema(description = "의류 상세설명 이미지(페이지)", requiredMode = Schema.RequiredMode.REQUIRED)
									   String detailUrl,
									   @Schema(description = "의류 사이즈 및 사이즈별 수량 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
									   List<GetClothesSizeResponse> clothesSizeList,
									   @Schema(description = "의류 Object 파일", requiredMode = Schema.RequiredMode.REQUIRED)
									   String url) {

	public static GetClothesDetailResponse from(Clothes clothes, String url) {
		return GetClothesDetailResponse.builder()
			.clothesId(clothes.getId())
			.category(clothes.getCategory())
			.imageUrl(clothes.getImageUrl())
			.name(clothes.getName())
			.price(clothes.getPrice())
			.genderCategory(clothes.getGenderCategory())
			.productNumber(clothes.getProductNumber())
			.discount(clothes.getDiscount())
			.detailUrl(clothes.getDetailUrl())
			.clothesSizeList(clothes.getClothesSizeList().stream().map(GetClothesSizeResponse::from).toList())
			.url(url)
			.build();
	}
}
