package com.example.demo.clothes.dto;

import java.util.List;

import com.example.demo.clothes.entity.ClothesSize;
import com.example.demo.clothes.entity.GenderCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "Clothes Detail Response", description = "특정 옷 상세 정보 조회 응답")
public record GetClothesDetailResponse(Long clothesId,
									   String category,
									   String imageUrl,
									   String name,
									   int price,
									   GenderCategory genderCategory,
									   String productNumber,
									   int discount,
									   String detailUrl,
									   List<ClothesSize> clothesSizeList) {
}
