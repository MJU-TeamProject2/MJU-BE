package com.example.demo.controller;

import com.example.demo.cart.dto.request.AddToCartItemRequest;
import com.example.demo.cart.dto.request.UpdateCartItemRequest;
import com.example.demo.cart.dto.response.GetCartResponse;
import com.example.demo.cart.service.CartService;
import com.example.demo.common.dto.SuccessResponse;
import com.example.demo.common.security.AuthInfo;
import com.example.demo.common.security.JwtInfo;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartControllerImpl implements CartController {

  private final CartService cartService;
  @Override
  @GetMapping
  public ResponseEntity<SuccessResponse<List<GetCartResponse>>> getCartItems(@AuthInfo JwtInfo jwtInfo) {
    return SuccessResponse.of(
        cartService.getCartItems(jwtInfo.customerId())).asHttp(HttpStatus.OK);
  }

  @Override
  @PostMapping
  public ResponseEntity<SuccessResponse<Void>> addToCartItem(@AuthInfo JwtInfo jwtInfo,
      @Valid @RequestBody AddToCartItemRequest addToCartItemRequest) {
    cartService.addToCart(jwtInfo.customerId(), addToCartItemRequest);
    return SuccessResponse.ofNoData().asHttp(HttpStatus.OK);
  }

  @Override
  @PatchMapping
  public ResponseEntity<SuccessResponse<Void>> updateCartItem(@AuthInfo JwtInfo jwtInfo,
      @Valid @RequestBody UpdateCartItemRequest updateCartItemRequest) {
    cartService.updateCartItem(jwtInfo.customerId(), updateCartItemRequest);
    return SuccessResponse.ofNoData().asHttp(HttpStatus.OK);
  }

  @Override
  @DeleteMapping("/{clothesId}")
  public ResponseEntity<SuccessResponse<Void>> deleteFromCart(@AuthInfo JwtInfo jwtInfo, @PathVariable Long clothesId) {
    cartService.deleteFromCart(jwtInfo.customerId(), clothesId);
    return SuccessResponse.ofNoData().asHttp(HttpStatus.OK);
  }
}