package com.example.demo.customer.dto.request;

import com.example.demo.customer.entity.CardProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
@Schema(name = "Add To Payment Request", description = "결제 수단 추가 요청")
public record AddPaymentRequest(
    @Schema(description = "카드 번호", example = "1234-5678-1234-5678")
    @NotBlank(message = "카드 번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{4}-\\d{4}-\\d{4}-\\d{4}$", message = "올바른 카드 번호 형식이 아닙니다. (예: 1234-5678-1234-5678)")
    String cardNumber,

    @Schema(description = "카드 제공사", example = "SHINHAN")
    @NotNull(message = "카드 제공사는 필수 입력값입니다.")
    CardProvider cardProvider,

    @Schema(description = "카드 유효기간", example = "12/25")
    @NotBlank(message = "카드 유효기간은 필수 입력값입니다.")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$", message = "올바른 유효기간 형식이 아닙니다. (예: MM/YY)")
    String expiryDate
) {
}
