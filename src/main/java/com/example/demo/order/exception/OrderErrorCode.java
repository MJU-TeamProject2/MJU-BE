package com.example.demo.order.exception;

import com.example.demo.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderErrorCode implements ErrorCode {
  ORDER_NOT_FOUND(404, "OR001", "주문목록에서 해당 상품을 찾을 수 없습니다."),
  ORDER_STATUS_NOT_VALID(409, "OR002", "주문 대기 상태에서만, 주소 변경이 가능합니다.");

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
