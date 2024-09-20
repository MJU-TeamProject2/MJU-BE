package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.common.dto.SuccessResponse;
import com.example.demo.customer.dto.request.RegisterRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Customer API", description = "Customer API")
public interface CustomerController{

	@Operation(summary = "고객 회원가입 API", description = "고객의 회원가입을 위한 API")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "성공적으로 메시지 반환")

	})
	@PostMapping(value = "/register")
	ResponseEntity<SuccessResponse<Void>> register(@RequestBody RegisterRequest registerRequest);

}
