package com.example.demo.controller;

import com.example.demo.common.dto.SuccessResponse;
import com.example.demo.common.security.AuthInfo;
import com.example.demo.common.security.JwtInfo;
import com.example.demo.order.dto.request.AddToOrderItemRequest;
import com.example.demo.order.dto.request.UpdateOrderItemRequest;
import com.example.demo.order.dto.response.GetOrderDetailResponse;
import com.example.demo.order.dto.response.GetOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@Tag(name = "Order API", description = "주문 API")
public interface OrderController {

  @Operation(summary = "주문 목록 조회", description = "주문 목록을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "성공적으로 조회 완료"),
  })
  @GetMapping
  ResponseEntity<SuccessResponse<List<GetOrderResponse>>> getOrderItems(@AuthInfo JwtInfo jwtInfo);

  @Operation(summary = "주문 상세 조회", description = "주문 상세를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "주문 상세를 성공적으로 조회 완료"),
  })
  @GetMapping("/{orderId}")
  ResponseEntity<SuccessResponse<GetOrderDetailResponse>> getOrderItemDetail(@AuthInfo JwtInfo jwtInfo, @PathVariable Long orderId);

  @Operation(summary = "주문 추가", description = "주문을 추가합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "주문이 성공적으로 추가 완료"),
  })
  @PostMapping
  ResponseEntity<SuccessResponse<Void>> addToOrderItem(@AuthInfo JwtInfo jwtInfo, @Valid @RequestBody AddToOrderItemRequest addToOrderItemRequest);

  @Operation(summary = "주문 수정", description = "주문을 수정합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "주문이 성공적으로 수정 완료"),
  })
  @PatchMapping
  ResponseEntity<SuccessResponse<Void>> updateOrderItem(@AuthInfo JwtInfo jwtInfo, @Valid @RequestBody UpdateOrderItemRequest updateOrderItemRequest);

  @Operation(summary = "주문 취소", description = "주문을 취소합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "주문이 성공적으로 취소 완료"),
  })
  @DeleteMapping("/{orderId}")
  ResponseEntity<SuccessResponse<Void>> deleteOrderItem(@AuthInfo JwtInfo jwtInfo, @PathVariable Long orderId);
}
