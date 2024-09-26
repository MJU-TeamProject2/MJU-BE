package com.example.demo.exception;

import com.example.demo.common.exception.CustomException;

public class CustomerNotFoundException extends CustomException {
	public CustomerNotFoundException() {
		super(CustomerErrorCode.CUSTOMER_NOT_FOUND);
	}
}
