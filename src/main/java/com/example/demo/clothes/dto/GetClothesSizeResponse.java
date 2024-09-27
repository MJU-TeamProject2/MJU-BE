package com.example.demo.clothes.dto;

import com.example.demo.clothes.entity.ClothesSize;
import com.example.demo.clothes.entity.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "Clothes Size Response", description = "의류 사이즈와 사이즈별 수량 응답")
public record GetClothesSizeResponse(@Schema(description = "의류 사이즈", requiredMode = Schema.RequiredMode.REQUIRED)
									 Size size,
									 @Schema(description = "의류 수량", requiredMode = Schema.RequiredMode.REQUIRED)
									 Integer quantity) {

	public static GetClothesSizeResponse from(ClothesSize clothesSize) {
		return GetClothesSizeResponse.builder()
			.size(clothesSize.getSize())
			.quantity(clothesSize.getQuantity())
			.build();
	}
}
