package com.example.demo.customer.dto.response;

import com.example.demo.customer.entity.CardProvider;
import com.example.demo.customer.entity.Payment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(name = "Get Payment Response", description = "결제 수단 조회 응답")
public record GetPaymentResponse(
    Long paymentId,
    String cardNumber,
    CardProvider cardProvider
) {
  static public List<GetPaymentResponse> listOf(List<Payment> payments) {
    return payments.stream()
        .map(payment -> GetPaymentResponse.builder()
            .paymentId(payment.getId())
            .cardNumber(payment.getCardNumber())
            .cardProvider(payment.getCardProvider()))
        .map(GetPaymentResponse.GetPaymentResponseBuilder::build)
        .toList();
  }
}
