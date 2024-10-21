package com.example.demo.common.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

	INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다."),
	INVALID_SERVER_ERROR(500, "C001", "서버 오류"),
	FILE_UPLOAD_FAILED(400, "C004", "파일 업로드에 실패했습니다.");

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
