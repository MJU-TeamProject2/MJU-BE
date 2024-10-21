package com.example.demo.cart.dto.response;

import com.example.demo.cart.entity.Cart;
import java.util.List;
import lombok.Builder;

@Builder
public record GetCartResponse(
    Long clothesId,
    String imageUrl,
    String detailUrl,
    String name,
    Integer quantity,
    Integer price,
    Integer discount
) {

  public static List<GetCartResponse> listOf(List<Cart> carts) {
    return carts.stream()
        .map(cart -> GetCartResponse.builder()
                .clothesId(cart.getId())
                .imageUrl(cart.getClothes().getImageUrl())
                .detailUrl(cart.getClothes().getDetailUrl())
                .name(cart.getClothes().getName())
                .quantity(cart.getQuantity())
                .price(cart.getClothes().getPrice())
                .discount(cart.getClothes().getDiscount()))
        .map(GetCartResponse.GetCartResponseBuilder::build)
        .toList();
  }
}
