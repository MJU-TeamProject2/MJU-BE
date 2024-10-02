package com.example.demo.common.dto;

import java.util.List;

import com.example.demo.common.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

	private final boolean success = false;
	private final ErrorInfo data;

	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(new ErrorInfo(errorCode.getCode(), errorCode.getMessage(), null));
	}

	private record ErrorInfo(String code,
							 String message,
							 @JsonInclude(JsonInclude.Include.NON_EMPTY)
							 List<String> errors) {
	}
}
