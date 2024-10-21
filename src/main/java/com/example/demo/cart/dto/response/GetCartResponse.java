package com.example.demo.cart.dto.response;

import com.example.demo.cart.entity.Cart;
import com.example.demo.clothes.entity.Size;
import java.util.List;
import lombok.Builder;

@Builder
public record GetCartResponse(
    Long cartId,
    Long clothesId,
    String imageUrl,
    String detailUrl,
    String name,
    Integer quantity,
    Integer price,
    Integer discount,
    Size size,
    Integer availableQuantity
) {

  public static List<GetCartResponse> listOf(List<Cart> carts) {
    return carts.stream()
        .map(cart -> GetCartResponse.builder()
                .cartId(cart.getId())
                .clothesId(cart.getId())
                .imageUrl(cart.getClothes().getImageUrl())
                .detailUrl(cart.getClothes().getDetailUrl())
                .name(cart.getClothes().getName())
                .quantity(cart.getQuantity())
                .price(cart.getClothes().getPrice())
                .discount(cart.getClothes().getDiscount())
                .size(cart.getClothesSize().getSize())
                .availableQuantity(cart.getClothesSize().getQuantity()))
        .map(GetCartResponse.GetCartResponseBuilder::build)
        .toList();
  }
}
