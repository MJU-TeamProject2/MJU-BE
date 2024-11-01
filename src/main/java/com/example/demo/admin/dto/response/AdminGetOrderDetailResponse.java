package com.example.demo.admin.dto.response;

import com.example.demo.clothes.entity.Size;
import com.example.demo.customer.entity.CardProvider;
import com.example.demo.order.entity.Order;
import com.example.demo.order.entity.OrderStatus;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record AdminGetOrderDetailResponse(
    Long orderId,
    Customer customer,
    Long clothesId,
    String imageUrl,
    String detailUrl,
    String name,
    Integer quantity,
    Integer price,
    Integer discount,
    Size size,
    OrderStatus orderStatus,
    Payment payment,
    Address address,
    LocalDateTime createAt
) {
  public record Customer(
      Long customerId,
      String name,
      String email,
      String phone
  ) {}

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

  public static AdminGetOrderDetailResponse from(Order order, String presignedImageUrl, String presignedDetailUrl) {
    return AdminGetOrderDetailResponse.builder()
        .orderId(order.getId())
        .customer(new Customer(
            order.getCustomer().getId(),
            order.getCustomer().getName(),
            order.getCustomer().getEmail(),
            order.getCustomer().getPhoneNumber()
        ))
        .clothesId(order.getClothes().getId())
        .imageUrl(presignedImageUrl)
        .detailUrl(presignedDetailUrl)
        .name(order.getClothes().getName())
        .quantity(order.getQuantity())
        .price(order.getClothes().getPrice())
        .discount(order.getClothes().getDiscount())
        .size(order.getClothesSize().getSize())
        .orderStatus(order.getStatus())
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