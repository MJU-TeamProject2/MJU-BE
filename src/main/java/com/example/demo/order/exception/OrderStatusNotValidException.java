package com.example.demo.order.exception;

import com.example.demo.common.exception.CustomException;

public class OrderStatusNotValidException extends CustomException {
  public OrderStatusNotValidException() {
    super(OrderErrorCode.ORDER_STATUS_NOT_VALID);
  }
}
