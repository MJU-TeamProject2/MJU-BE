package com.example.demo.exception;

import com.example.demo.common.exception.CustomException;

public class CartNotFoundException extends CustomException {

  public CartNotFoundException() {
    super(CartErrorCode.CART_NOT_FOUND);
  }
}
