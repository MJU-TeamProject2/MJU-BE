package com.example.demo.cart.dto.request;

import com.example.demo.clothes.entity.Size;
import com.example.demo.clothes.util.SizeValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(name = "Add To Cart Item Request", description = "장바구니에 상품 추가 요청")
public record AddToCartItemRequest(
    @Schema(description = "상품 id",  requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "상품 id는 비어있을 수 없습니다.")
    Long clothesId,

    @Schema(description = "변경할 수량",  requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
    @Max(value = 99, message = "수량은 99 이하이어야 합니다.")
    @NotNull(message = "수량은 비어있을 수 없습니다.")
    Integer quantity,

    @Schema(description = "상품 사이즈", requiredMode = Schema.RequiredMode.REQUIRED,
        example = "S, M, L, XL, XXL", implementation = Size.class)
    @SizeValidation.ValidSize
    Size size
) {}
