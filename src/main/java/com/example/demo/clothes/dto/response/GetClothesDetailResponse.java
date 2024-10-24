package com.example.demo.clothes.dto.response;

import java.util.List;

import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.entity.ClothesCategory;
import com.example.demo.clothes.entity.GenderCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "Clothes Detail Response", description = "특정 의류 상세 정보 조회 응답")
public record GetClothesDetailResponse(@Schema(description = "의류 id")
									   Long clothesId,
									   @Schema(description = "의류 카테고리")
									   ClothesCategory category,
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
									   Integer discount,
									   @Schema(description = "의류 상세설명 이미지(페이지)")
									   String detailUrl,
									   @Schema(description = "의류 사이즈 및 사이즈별 수량 리스트")
									   List<GetClothesSizeResponse> clothesSizeList,
									   @Schema(description = "의류 Object 파일")
									   String objectUrl,
									   @Schema(description = "의류 mtl 파일")
									   String mtlUrl) {

	public static GetClothesDetailResponse of(Clothes clothes, String imageUrl, String detailUrl, String objectUrl,
		String mtlUrl) {
		return GetClothesDetailResponse.builder()
			.clothesId(clothes.getId())
			.category(clothes.getCategory())
			.imageUrl(imageUrl)
			.name(clothes.getName())
			.price(clothes.getPrice())
			.genderCategory(clothes.getGenderCategory())
			.productNumber(clothes.getProductNumber())
			.discount(clothes.getDiscount())
			.detailUrl(detailUrl)
			.clothesSizeList(clothes.getClothesSizeList().stream().map(GetClothesSizeResponse::from).toList())
			.objectUrl(objectUrl)
			.mtlUrl(mtlUrl)
			.build();
	}
}
