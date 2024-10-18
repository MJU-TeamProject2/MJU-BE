package com.example.demo.clothes.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.clothes.entity.ClothesCategory;
import com.example.demo.clothes.entity.GenderCategory;
import com.example.demo.clothes.entity.Size;
import com.example.demo.clothes.util.ClotheCategoryValidation;
import com.example.demo.clothes.util.GenderCategoryValidation;
import com.example.demo.clothes.util.SizeValidation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(name = "CreateClothesRequest", description = "상품 수정 요청")
public record UpdateClothesRequest(
	@Schema(description = "상품 카테고리", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		example = "SHOES, TOPS, OUTERWEAR, PANTS, DRESSES",
		implementation = ClothesCategory.class)
	@ClotheCategoryValidation.ValidClotheCategory
	ClothesCategory category,
	@Schema(description = "상품 이름", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		example = "티셔츠")
	String name,
	@Schema(description = "상품 가격", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		example = "10000")
	@Min(value = 0, message = "가격은 0 이상이어야 합니다")
	Integer price,
	@Schema(description = "성별 카테고리", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		example = "MALE, FEMALE, UNISEX", implementation = GenderCategory.class)
	@GenderCategoryValidation.ValidGenderCategory
	GenderCategory genderCategory,
	@Schema(description = "상품 번호", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		example = "PRODUCT-001")
	String productNumber,
	@Schema(description = "상품 할인율", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		example = "20")
	@Min(value = 0, message = "할인율은 0 이상이어야 합니다")
	@Max(value = 100, message = "할인율은 100 이하여야 합니다")
	Integer discount,
	@Schema(description = "상품 사이즈", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		example = "S, M, L, XL, XXL", implementation = Size.class)
	@SizeValidation.ValidSize
	Size size,
	@Schema(description = "상품 재고", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		example = "100")
	@Min(value = 0, message = "재고는 0 이상이어야 합니다.")
	Integer quantity,
	@Schema(description = "상품 메인 이미지", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	MultipartFile mainImage,
	@Schema(description = "상품 상세설명 이미지", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	MultipartFile detailImage,
	@Schema(description = "상품 3D Object", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	MultipartFile objectFile) {
}
