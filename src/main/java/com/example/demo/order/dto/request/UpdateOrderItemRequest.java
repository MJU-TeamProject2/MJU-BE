package com.example.demo.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(name = "Update Order Item Request", description = "주문 주소 수정 요청")
public record UpdateOrderItemRequest(
   @Schema(description = "주문 품목 ID", example = "1")
   @Min(value = 0, message = "0 이상의 값을 입력해주세요.")
   @Max(value = Long.MAX_VALUE, message = "최대 " + Long.MAX_VALUE + "까지 입력할 수 있습니다.")
   @NotNull(message = "주문 품목 ID는 필수 입력값입니다.")
   Long orderId,

   @Schema(description = "주소지 변경", example = "1")
   @NotNull(message = "주소지 번호는 필수 입력값입니다.")
   @Min(value = 0, message = "주소지 번호는 0보다 커야합니다.")
   @Max(value = Long.MAX_VALUE, message = "최대 " + Long.MAX_VALUE + "까지 입력할 수 있습니다.")
   Long addressId
) {
}
