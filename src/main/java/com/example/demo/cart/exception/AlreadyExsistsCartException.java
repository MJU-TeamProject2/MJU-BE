package com.example.demo.cart.exception;

import com.example.demo.common.exception.CustomException;

public class AlreadyExsistsCartException extends CustomException {

  public AlreadyExsistsCartException() {
    super(CartErrorCode.ALREADY_EXISTS_CART);
  }
}
