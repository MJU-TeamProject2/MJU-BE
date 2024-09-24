package com.example.demo.exception;

import com.example.demo.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CustomerErrorCode implements ErrorCode {

	CUSTOMER_NOT_FOUND(400, "C001", "고객을 찾을 수 없습니다"),
	CUSTOMER_WRONG_PASSWORD(400, "C002", "고객의 비밀번호가 일치하지 않습니다."),
	CUSTOMER_AUTH_NOT_FOUND(400, "C002", "refresh token을 찾을 수 없습니다."),
	CUSTOMER_EMAIL_DUPLICATE(400, "C003", "이미 가입한 이메일입니다.");

	private final int status;
	private final String code;
	private final String message;

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
