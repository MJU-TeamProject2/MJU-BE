package com.example.demo.cart.dto.response;

import com.example.demo.cart.entity.Cart;
import java.util.List;

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
        .map(cart -> new GetCartResponse(
            cart.getClothes().getId(),
            cart.getClothes().getImageUrl(),
            cart.getClothes().getDetailUrl(),
            cart.getClothes().getName(),
            cart.getQuantity(),
            cart.getClothes().getPrice(),
            cart.getClothes().getDiscount()
        ))
        .toList();
  }
}
