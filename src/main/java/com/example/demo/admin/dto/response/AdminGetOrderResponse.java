package com.example.demo.admin.dto.response;

import com.example.demo.clothes.entity.Size;
import com.example.demo.order.entity.Order;
import com.example.demo.order.entity.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(name = "Admin Get Order Response", description = "관리자 주문 목록 조회 응답")
public record AdminGetOrderResponse(
    Long orderId,
    Long customerId,
    String customerName,
    Long clothesId,
    String imageUrl,
    String detailUrl,
    String name,
    Integer quantity,
    Integer price,
    Integer discount,
    Size size,
    OrderStatus orderStatus,
    LocalDateTime createdAt
) {
  public static List<AdminGetOrderResponse> listOf(List<Order> orders) {
    return orders.stream()
        .map(order -> AdminGetOrderResponse.builder()
            .orderId(order.getId())
            .customerId(order.getCustomer().getId())
            .customerName(order.getCustomer().getName())
            .clothesId(order.getClothes().getId())
            .imageUrl(order.getClothes().getImageUrl())
            .detailUrl(order.getClothes().getDetailUrl())
            .name(order.getClothes().getName())
            .quantity(order.getQuantity())
            .price(order.getClothes().getPrice())
            .discount(order.getClothes().getDiscount())
            .size(order.getClothesSize().getSize())
            .orderStatus(order.getStatus())
            .createdAt(order.getCreatedAt())
            .build())
        .toList();
  }

  public static AdminGetOrderResponse from(Order order, String presignedImageUrl, String presignedDetailUrl) {
    return AdminGetOrderResponse.builder()
        .orderId(order.getId())
        .customerId(order.getCustomer().getId())
        .customerName(order.getCustomer().getName())
        .clothesId(order.getClothes().getId())
        .imageUrl(presignedImageUrl)
        .detailUrl(presignedDetailUrl)
        .name(order.getClothes().getName())
        .quantity(order.getQuantity())
        .price(order.getClothes().getPrice())
        .discount(order.getClothes().getDiscount())
        .size(order.getClothesSize().getSize())
        .orderStatus(order.getStatus())
        .createdAt(order.getCreatedAt())
        .build();
  }
}