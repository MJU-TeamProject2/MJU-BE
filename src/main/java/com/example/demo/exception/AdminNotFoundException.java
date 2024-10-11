package com.example.demo.exception;

import com.example.demo.common.exception.CustomException;

public class AdminNotFoundException extends CustomException {
	public AdminNotFoundException() {
		super(AdminErrorCode.ADMIN_NOT_FOUND);
	}
}
