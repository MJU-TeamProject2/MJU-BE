package com.example.demo.cart.controller;

import com.example.demo.cart.dto.request.AddToCartItemRequest;
import com.example.demo.cart.dto.request.UpdateCartItemRequest;
import com.example.demo.common.dto.SuccessResponse;
import com.example.demo.common.security.AuthInfo;
import com.example.demo.common.security.JwtInfo;
import com.example.demo.cart.dto.response.GetCartResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Cart API", description = "장바구니 API")
public interface CartController {

  @Operation(summary = "장바구니 조회", description = "장바구니 조회를 위한 API")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "성공적으로 조회 완료 및, 장바구니 목록에 아이템이 존재하는 경우"),
  })
  @GetMapping()
  ResponseEntity<SuccessResponse<List<GetCartResponse>>> getCartItems(@AuthInfo JwtInfo jwtInfo);

  @Operation(summary = "장바구니에 상품 추가", description = "장바구니에 상품 추가를 위한 API")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "장바구니에 상품이 성공적으로 추가 완료")
  })
  @PostMapping()
  ResponseEntity<SuccessResponse<Void>> addToCartItem(@AuthInfo JwtInfo jwtInfo, @Valid @RequestBody AddToCartItemRequest addToCartItemRequest);

  @Operation(summary = "장바구니 상품 수정", description = "장바구니 상품 수정을 위한 API")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "장바구니 상품이 성공적으로 수정 완료")
  })
  @PatchMapping()
  ResponseEntity<SuccessResponse<Void>> updateCartItem(@AuthInfo JwtInfo jwtInfo, @Valid @RequestBody UpdateCartItemRequest updateCartItemRequest);

  @Operation(summary = "장바구니 상품 삭제", description = "장바구니 상품 삭제를 위한 API")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "장바구니 상품이 성공적으로 삭제 완료")
  })
  @DeleteMapping("/{cartId}")
  ResponseEntity<SuccessResponse<Void>> deleteFromCart(@AuthInfo JwtInfo jwtInfo, @PathVariable Long cartId);
}
