package com.example.demo.exception;

import com.example.demo.common.exception.CustomException;

public class CustomerAuthNotFoundException extends CustomException {
	public CustomerAuthNotFoundException() {
		super(CustomerErrorCode.CUSTOMER_AUTH_NOT_FOUND);
	}
}
