package com.example.demo.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.admin.dto.request.AdminLoginRequest;
import com.example.demo.admin.dto.response.AdminLoginResponse;
import com.example.demo.admin.service.AdminService;
import com.example.demo.clothes.dto.request.CreateClothesRequest;
import com.example.demo.common.dto.SuccessResponse;
import com.example.demo.common.security.CustomCookieName;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminControllerImpl implements AdminController {

	private final AdminService adminService;

	@Override
	public ResponseEntity<SuccessResponse<AdminLoginResponse>> login(AdminLoginRequest adminLoginRequest) {
		AdminLoginResponse adminLoginResponse = adminService.login(adminLoginRequest.code(), adminLoginRequest.password());
		var refreshTokenResponseCookie = getRefreshTokenResponseCookie(adminLoginResponse.refreshToken());
		return SuccessResponse.of(adminLoginResponse).okWithCookie(refreshTokenResponseCookie);
	}

	@Override
	public ResponseEntity<SuccessResponse<Void>> registrationProduct(CreateClothesRequest createClothesRequest) {
		return null;
	}

	private ResponseCookie getRefreshTokenResponseCookie(String refreshToken) {
		return ResponseCookie.from(CustomCookieName.REFRESH_TOKEN, refreshToken)
			.httpOnly(true)
			.secure(true)
			.path("/")
			.build();
	}

}
