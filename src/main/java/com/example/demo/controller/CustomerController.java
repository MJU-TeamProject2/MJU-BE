package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Customer API", description = "Customer API")
public interface CustomerController{

	@Operation(summary = "", description = "")
	@ApiResponse(responseCode = "200", description = "성공적으로 메시지 반환")
	@PostMapping(value = "/register")
	void register();

}
