package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.admin.dto.request.AdminLoginRequest;
import com.example.demo.admin.dto.response.AdminLoginResponse;
import com.example.demo.common.dto.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin API", description = "관리자 API")
public interface AdminController {

	@Operation(summary = "관리자 로그인 API", description = "관리자의 로그인을 위한 API")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "성공적으로 로그인 완료")
	})
	@PostMapping(value = "/login")
	ResponseEntity<SuccessResponse<AdminLoginResponse>> login(@RequestBody AdminLoginRequest adminLoginRequest);

}
