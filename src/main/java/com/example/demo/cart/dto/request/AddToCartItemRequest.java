package com.example.demo.cart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(name = "Add To Cart Item Request", description = "장바구니에 상품 추가 요청")
public record AddToCartItemRequest(
    @Schema(description = "상품 id",  requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "상품 id는 비어있을 수 없습니다.")
    Long clothesId
) {}
