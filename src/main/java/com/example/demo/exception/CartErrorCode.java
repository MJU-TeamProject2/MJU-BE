package com.example.demo.exception;

import com.example.demo.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CartErrorCode implements ErrorCode {
  CART_NOT_FOUND(404, "CA001", "장바구니에서 해당 상품을 찾을 수 있없습니다.");

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
