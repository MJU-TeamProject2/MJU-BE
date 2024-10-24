package com.example.demo.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(name = "Add To Order Item Request", description = "주문 품목 추가 요청")
public record AddToOrderItemRequest(
    @Schema(description = "장바구니 품목 ID", example = "1")
    @Min(value = 0, message = "0 이상의 값을 입력해주세요.")
    @Max(value = Long.MAX_VALUE, message = "최대 " + Long.MAX_VALUE + "까지 입력할 수 있습니다.")
    @NotNull(message = "장바구니 품목 ID는 필수 입력값입니다.")
    Long cartId,

    @Schema(description = "주소 ID", example = "1")
    @Min(value = 0, message = "0 이상의 값을 입력해주세요.")
    @Max(value = Long.MAX_VALUE, message = "최대 " + Long.MAX_VALUE + "까지 입력할 수 있습니다.")
    @NotNull(message = "주소 ID는 필수 입력값입니다.")
    Long AddressId,

    @Schema(description = "결제수단 ID", example = "1")
    @Min(value = 0, message = "0 이상의 값을 입력해주세요.")
    @Max(value = Long.MAX_VALUE, message = "최대 " + Long.MAX_VALUE + "까지 입력할 수 있습니다.")
    @NotNull(message = "결제수단 ID는 필수 입력값입니다.")
    Long paymentId
) {
}
