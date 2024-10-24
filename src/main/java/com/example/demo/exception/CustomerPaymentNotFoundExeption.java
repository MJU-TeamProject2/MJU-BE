package com.example.demo.exception;

import com.example.demo.common.exception.CustomException;

public class CustomerPaymentNotFoundExeption extends CustomException {
  public CustomerPaymentNotFoundExeption() {
    super(CustomerErrorCode.CUSTOMER_PAYMENT_NOT_FOUND);
  }
}
