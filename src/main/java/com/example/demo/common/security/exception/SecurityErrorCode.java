package com.example.demo.common.security.exception;

import com.example.demo.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SecurityErrorCode implements ErrorCode {
	NON_BEARER(401, "S001", "Authorization header에 Bearer 토큰이 없습니다."),
	EXPIRED_TOKEN(403, "S002", "토큰이 만료됐습니다."),
	INVALID_TOKEN(403, "S003", "토큰이 유효하지 않습니다."),
	DISALLOWED_TOKEN_TYPE(403, "S004", "허용되지 않은 토큰 타입입니다."),
	AUTHORITY_NOT_FOUND(403, "S005", "접근 권한이 없습니다.");

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

