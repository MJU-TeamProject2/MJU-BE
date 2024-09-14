package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Test API", description = "테스트 API")
public interface TestControllerInterface {

	@Operation(summary = "Hello 메시지 반환", description = "간단한 Hello 메시지를 반환하는 테스트 API")
	@ApiResponse(responseCode = "200", description = "성공적으로 메시지 반환")
	@GetMapping("/hello")
	String hello();

}
