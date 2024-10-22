package com.example.demo.exception;

import com.example.demo.common.exception.CustomException;

public class CustomerAddressNotFoundException extends CustomException {
  public CustomerAddressNotFoundException() {
    super(CustomerErrorCode.CUSTOMER_ADDRESS_NOT_FOUND);
  }
}
