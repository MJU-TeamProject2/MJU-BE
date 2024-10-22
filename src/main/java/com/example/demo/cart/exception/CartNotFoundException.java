package com.example.demo.cart.exception;

import com.example.demo.common.exception.CustomException;

public class CartNotFoundException extends CustomException {

  public CartNotFoundException() {
    super(CartErrorCode.CART_NOT_FOUND);
  }
}
