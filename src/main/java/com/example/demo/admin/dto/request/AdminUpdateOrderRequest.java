package com.example.demo.admin.dto.request;

import com.example.demo.order.entity.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(name = "Admin Update Order Request", description = "관리자 주문 상태 수정 요청")
public record AdminUpdateOrderRequest(
    @Schema(description = "주문 ID", example = "1")
    @Min(value = 0, message = "0 이상의 값을 입력해주세요.")
    @Max(value = Long.MAX_VALUE, message = "최대 " + Long.MAX_VALUE + "까지 입력할 수 있습니다.")
    @NotNull(message = "주문 ID는 필수 입력값입니다.")
    Long orderId,

    @Schema(description = "주문 상태", example = "SHIPPED")
    @NotNull(message = "주문 상태는 필수 입력값입니다.")
    OrderStatus orderStatus
) {
}
