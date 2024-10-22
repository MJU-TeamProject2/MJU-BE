package com.example.demo.order.dto.response;

import com.example.demo.clothes.entity.Size;
import com.example.demo.order.entity.Order;
import com.example.demo.order.entity.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

import java.util.List;
import lombok.Builder;

@Builder
@Schema(name = "Get Order Response", description = "주문 조회 응답")
public record GetOrderResponse(
    Long orderId,
    Long clothesId,
    String imageUrl,
    String name,
    Integer quantity,
    Integer price,
    Integer discount,
    Size size,
    OrderStatus orderStatus,
    LocalDateTime createdAt
) {
  public static List<GetOrderResponse> listOf(List<Order> orders) {
    return orders.stream()
        .map(order -> GetOrderResponse.builder()
                .orderId(order.getId())
                .clothesId(order.getClothes().getId())
                .imageUrl(order.getClothes().getImageUrl())
                .name(order.getClothes().getName())
                .quantity(order.getQuantity())
                .price(order.getClothes().getPrice())
                .discount(order.getClothes().getDiscount())
                .size(order.getClothesSize().getSize())
                .orderStatus(order.getStatus())
                .createdAt(order.getCreatedAt()))
        .map(GetOrderResponse.GetOrderResponseBuilder::build)
        .toList();
  }
}
