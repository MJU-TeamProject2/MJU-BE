package com.example.demo.exception;

import com.example.demo.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AdminErrorCode implements ErrorCode{

	ADMIN_WRONG_PASSWORD(400, "A001", "관리자를 찾을 수 없습니다.");

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
