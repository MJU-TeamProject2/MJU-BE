package com.example.demo.cart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(name = "Update Cart Item Request", description = "장바구니 상품 수량 변경 요청")
public record UpdateCartItemRequest(
    @Schema(description = "상품 id", required = true)
    @NotNull(message = "상품 id는 비어있을 수 없습니다.")
    Long clothesId,

    @Schema(description = "변경할 수량", required = true)
    @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
    @Max(value = 99, message = "수량은 99 이하이어야 합니다.")
    @NotNull(message = "수량은 비어있을 수 없습니다.")
    Integer quantity
) {}
