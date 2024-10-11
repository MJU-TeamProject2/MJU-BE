package com.example.demo.clothes.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.clothes.entity.ClothesCategory;
import com.example.demo.clothes.entity.GenderCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "CreateClothesRequest", description = "상품 등록 요청")
public record CreateClothesRequest(
								   @Schema(description = "상품 카테고리", requiredMode = Schema.RequiredMode.REQUIRED,
								   example = "SHOES, TOPS, OUTERWEAR, PANTS, DRESSES")
								   @NotNull(message = "카테고리는 필수입니다")
								   ClothesCategory category,
								   @Schema(description = "상품 이름", requiredMode = Schema.RequiredMode.REQUIRED,
								   example = "티셔츠")
								   @NotNull(message = "이름은 필수입니다")
								   @Size(min = 1, max = 100, message = "이름은 1-100자 사이여야 합니다")
								   String name,
								   @Schema(description = "상품 가격", requiredMode = Schema.RequiredMode.REQUIRED,
								   example = "10000")
								   @NotNull(message = "가격은 필수입니다")
								   @Min(value = 0, message = "가격은 0 이상이어야 합니다")
								   Integer price,
								   @Schema(description = "성별 카테고리", requiredMode = Schema.RequiredMode.REQUIRED,
								   example = "MALE, FEMALE, UNISEX")
								   @NotNull(message = "성별 카테고리는 필수입니다")
								   GenderCategory genderCategory,
								   @Schema(description = "상품 번호", requiredMode = Schema.RequiredMode.REQUIRED,
								   example = "PRODUCT-001")
								   @NotNull(message = "상품 번호는 필수입니다")
								   String productNumber,
								   @Schema(description = "상품 할인율", requiredMode = Schema.RequiredMode.REQUIRED,
								   example = "20")
								   @Min(value = 0, message = "할인율은 0 이상이어야 합니다")
								   @Max(value = 100, message = "할인율은 100 이하여야 합니다")
								   Integer discount,
								   @Schema(description = "상품 사이즈", requiredMode = Schema.RequiredMode.REQUIRED,
									   example = "S, M, L, XL, XXL")
								   @NotNull(message = "최소 하나의 사이즈는 필수입니다")
								   @NotEmpty(message = "최소 하나의 사이즈는 필수입니다")
								   List<Size> sizes,
								   @Schema(description = "상품 메인 이미지", requiredMode = Schema.RequiredMode.REQUIRED)
								   @NotNull(message = "메인 이미지는 필수입니다")
								   MultipartFile mainImage,
								   @Schema(description = "상품 메인 이미지", requiredMode = Schema.RequiredMode.REQUIRED)
								   @NotNull(message = "상세 이미지는 필수입니다")
								   MultipartFile detailImage) {
}
