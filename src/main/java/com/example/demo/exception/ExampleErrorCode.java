package com.example.demo.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExampleErrorCode implements ErrorCode{

	ENTITY1_NOT_FOUND(400, "E001", "엔티티1을 찾을 수 없습니다"),
	ENTITY2_NOT_FOUND(400, "E002", "엔티티2 찾을 수 없습니다");

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
