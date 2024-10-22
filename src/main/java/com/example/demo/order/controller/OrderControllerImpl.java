package com.example.demo.order.controller;

import com.example.demo.common.dto.SuccessResponse;
import com.example.demo.common.security.AuthInfo;
import com.example.demo.common.security.JwtInfo;
import com.example.demo.order.dto.request.AddToOrderItemRequest;
import com.example.demo.order.dto.request.UpdateOrderItemRequest;
import com.example.demo.order.dto.response.GetOrderDetailResponse;
import com.example.demo.order.dto.response.GetOrderResponse;
import com.example.demo.order.service.application.OrderApplicationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
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
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {

  private final OrderApplicationService orderApplicationService;

  @Override
  @GetMapping
  public ResponseEntity<SuccessResponse<List<GetOrderResponse>>> getOrderItems(@AuthInfo JwtInfo jwtInfo) {
    return SuccessResponse.of(
        orderApplicationService.getOrderItems(jwtInfo.memberId())).asHttp(HttpStatus.OK);
  }

  @Override
  @GetMapping("/{orderId}")
  public ResponseEntity<SuccessResponse<GetOrderDetailResponse>> getOrderItemDetail(@AuthInfo JwtInfo jwtInfo,
      @PathVariable Long orderId) {
    return SuccessResponse.of(
        orderApplicationService.getOrderItemDetail(jwtInfo.memberId(), orderId)).asHttp(HttpStatus.OK);
  }

  @Override
  @PostMapping
  public ResponseEntity<SuccessResponse<Void>> addToOrderItem(@AuthInfo JwtInfo jwtInfo,
      @Valid @RequestBody AddToOrderItemRequest addToOrderItemRequest) {
    orderApplicationService.addToOrderItem(jwtInfo.memberId(), addToOrderItemRequest);
    return SuccessResponse.ofNoData().asHttp(HttpStatus.OK);
  }

  @Override
  @PatchMapping
  public ResponseEntity<SuccessResponse<Void>> updateOrderItem(@AuthInfo JwtInfo jwtInfo,
    @Valid @RequestBody  UpdateOrderItemRequest updateOrderItemRequest) {
    orderApplicationService.updateOrderItem(jwtInfo.memberId(), updateOrderItemRequest);
    return SuccessResponse.ofNoData().asHttp(HttpStatus.OK);
  }

  @Override
  @DeleteMapping("/{orderId}")
  public ResponseEntity<SuccessResponse<Void>> deleteOrderItem(@AuthInfo JwtInfo jwtInfo, @PathVariable Long orderId) {
    orderApplicationService.deleteOrderItem(jwtInfo.memberId(), orderId);
    return SuccessResponse.ofNoData().asHttp(HttpStatus.OK);
  }
}
