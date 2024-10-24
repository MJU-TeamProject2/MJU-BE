package com.example.demo.customer.dto.request;

import com.example.demo.customer.entity.CardProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
@Schema(name = "Update Request", description = "결제수단 수정 요청")
public record UpdatePaymentRequest(
    @Schema(description = "결제수단 ID", example = "1")
    @Min(value = 0, message = "0 이상의 값을 입력해주세요.")
    @Max(value = Long.MAX_VALUE, message = "최대 " + Long.MAX_VALUE + "까지 입력할 수 있습니다.")
    @NotNull(message = "결제수단 ID는 필수 입력값입니다.")
    Long paymentId,

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
