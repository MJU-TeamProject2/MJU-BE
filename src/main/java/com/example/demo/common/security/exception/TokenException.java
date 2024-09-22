package com.example.demo.common.security.exception;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;

import lombok.Getter;

@Getter
public class TokenException extends CustomException {

	public TokenException(ErrorCode errorCode) {
		super(errorCode);
	}

	public TokenException(ErrorCode errorCode, Throwable throwable) {
		super(errorCode, throwable);
	}

	public TokenException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}
}
