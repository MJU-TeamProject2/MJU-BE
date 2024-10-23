package com.example.demo.customer.dto.response;

import com.example.demo.customer.entity.CardProvider;
import com.example.demo.customer.entity.Payment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "Get Payment Detail Response", description = "결제 수단 상세 조회 응답")
public record GetPaymentDetailResponse(
    Long paymentId,
    String cardNumber,
    CardProvider cardProvider,
    String expiryDate
) {
  public static GetPaymentDetailResponse from(Payment payment) {
    return GetPaymentDetailResponse.builder()
        .paymentId(payment.getId())
        .cardNumber(payment.getCardNumber())
        .cardProvider(payment.getCardProvider())
        .expiryDate(payment.getExpiryDate())
        .build();
  }
}
