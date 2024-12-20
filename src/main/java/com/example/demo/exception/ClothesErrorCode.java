package com.example.demo.exception;

import com.example.demo.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ClothesErrorCode implements ErrorCode {

	CLOTHES_NOT_FOUND(404, "CT001", "해당 옷을 찾을 수 없습니다."),
	CLOTHES_DUPLICATE(405, "CT002", "이미 존재하는 의상입니다."),
	CLOTHES_SIZE_NOT_FOUND(404, "CT003", "해당 옷 사이즈를 찾을 수 없습니다."),
	CLOTHES_INSUFFICIENT_STOCK(422, "CT004", "상품의 재고가 부족합니다.");

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
