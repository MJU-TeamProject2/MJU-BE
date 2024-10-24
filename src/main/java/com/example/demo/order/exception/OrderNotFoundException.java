package com.example.demo.order.exception;

import com.example.demo.common.exception.CustomException;

public class OrderNotFoundException extends CustomException {

  public OrderNotFoundException() {
    super(OrderErrorCode.ORDER_NOT_FOUND);
  }
}
