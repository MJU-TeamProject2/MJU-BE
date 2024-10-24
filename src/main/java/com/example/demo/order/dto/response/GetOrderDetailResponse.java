package com.example.demo.order.dto.response;

import com.example.demo.clothes.entity.Size;
import com.example.demo.customer.entity.CardProvider;
import com.example.demo.order.entity.Order;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record GetOrderDetailResponse(
    Long orderId,
    Long clothesId,
    String imageUrl,
    String detailUrl,
    String name,
    Integer quantity,
    Integer price,
    Integer discount,
    Size size,
    Payment payment,
    Address address,
    LocalDateTime createAt
) {

  public record Payment(
      String cardNumber,
      CardProvider cardProvider
  ) {}

  public record Address(
      Long addressId,
      String recipient,
      String zipCode,
      String baseAddress,
      String detailAddress
  ) {}

  public static GetOrderDetailResponse from(Order order) {
    return GetOrderDetailResponse.builder()
        .orderId(order.getId())
        .clothesId(order.getClothes().getId())
        .imageUrl(order.getClothes().getImageUrl())
        .detailUrl(order.getClothes().getDetailUrl())
        .name(order.getClothes().getName())
        .quantity(order.getQuantity())
        .price(order.getClothes().getPrice())
        .discount(order.getClothes().getDiscount())
        .size(order.getClothesSize().getSize())
        .payment(new Payment(
            order.getPayment().getCardNumber(),
            order.getPayment().getCardProvider()
        ))
        .address(new Address(
            order.getAddress().getId(),
            order.getAddress().getRecipient(),
            order.getAddress().getZipCode(),
            order.getAddress().getBaseAddress(),
            order.getAddress().getDetailAddress()
        ))
        .createAt(order.getCreatedAt())
        .build();
  }

}
