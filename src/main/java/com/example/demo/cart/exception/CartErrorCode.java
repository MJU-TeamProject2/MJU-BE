package com.example.demo.cart.exception;

import com.example.demo.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CartErrorCode implements ErrorCode {
  CART_NOT_FOUND(404, "CA001", "장바구니에서 해당 상품을 찾을 수 없습니다."),
  ALREADY_EXISTS_CART(409, "CA002", "이미 장바구니에 존재하는 상품입니다.");

  private final int status;
  private final String code;
  private final String message;

  @Override
  public int getStatus() {
    return status;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
