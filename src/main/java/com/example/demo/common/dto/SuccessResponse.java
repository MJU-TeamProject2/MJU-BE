package com.example.demo.common.dto;

import static org.springframework.http.HttpHeaders.*;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import com.example.demo.clothes.dto.GetClothesObject;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse<T> {

	private final boolean success = true;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;

	public static <T> SuccessResponse<T> of(T data) {
		SuccessResponse<T> successResponse = new SuccessResponse<>();

		successResponse.data = data;

		return successResponse;
	}

	public static SuccessResponse<Void> ofNoData() {
		return new SuccessResponse<>();
	}

	public ResponseEntity<SuccessResponse<T>> asHttp(HttpStatus httpStatus) {
		return ResponseEntity.status(httpStatus).body(this);
	}

	public ResponseEntity<SuccessResponse<T>> okWithCookie(ResponseCookie responseCookie) {
		return ResponseEntity.ok()
			.header(SET_COOKIE, responseCookie.toString())
			.body(this);
	}

	public ResponseEntity<SuccessResponse<T>> okWithByteFile(GetClothesObject getClothesObject) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(ContentDisposition.builder("attachment")
			.filename(getClothesObject.fileName())
			.build());
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		return ResponseEntity.ok()
			.headers(headers)
			.body(this);
	}
}
