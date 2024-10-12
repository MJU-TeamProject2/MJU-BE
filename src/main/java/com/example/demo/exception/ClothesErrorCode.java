package com.example.demo.exception;

import com.example.demo.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ClothesErrorCode implements ErrorCode {

	CLOTHES_NOT_FOUND(404, "CT001", "해당 옷을 찾을 수 없습니다."),
	CLOTHES_DUPLICATE(405, "CT002", "이미 존재하는 의상입니다.");

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
