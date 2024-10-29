package com.example.demo.controller;

import com.example.demo.admin.dto.request.AdminUpdateOrderRequest;
import com.example.demo.admin.dto.response.AdminGetOrderDetailResponse;
import com.example.demo.admin.dto.response.AdminGetOrderResponse;
import com.example.demo.admin.service.application.AdminOrderApplicationService;
import com.example.demo.common.security.AuthInfo;
import com.example.demo.common.security.JwtInfo;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.admin.dto.request.AdminLoginRequest;
import com.example.demo.admin.dto.response.AdminLoginResponse;
import com.example.demo.admin.service.AdminService;
import com.example.demo.common.dto.SuccessResponse;
import com.example.demo.common.security.CustomCookieName;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminControllerImpl implements AdminController {

	private final AdminService adminService;
	private final AdminOrderApplicationService adminOrderApplicationService;

	@Override
	public ResponseEntity<SuccessResponse<AdminLoginResponse>> login(AdminLoginRequest adminLoginRequest) {
		AdminLoginResponse adminLoginResponse = adminService.login(adminLoginRequest.code(), adminLoginRequest.password());
		var refreshTokenResponseCookie = getRefreshTokenResponseCookie(adminLoginResponse.refreshToken());
		return SuccessResponse.of(adminLoginResponse).okWithCookie(refreshTokenResponseCookie);
	}

	@Override
	@GetMapping("/orders")
	public ResponseEntity<SuccessResponse<List<AdminGetOrderResponse>>> getOrders(
			@AuthInfo JwtInfo jwtInfo,
			@Parameter(description = "사용자 ID", example = "1")
			@RequestParam(required = false) Long memberId,
			@Parameter(description = "페이지 크기", example = "20")
			@RequestParam(value = "size", required = false, defaultValue = "20") int size,
			@Parameter(description = "페이지 번호", example = "0")
			@RequestParam(value = "page", required = false, defaultValue = "0") int page
	) {
		return SuccessResponse.of(
				adminOrderApplicationService.getOrders(jwtInfo.memberId(), memberId, PageRequest.of(page, size))
		).asHttp(HttpStatus.OK);
	}

	@Override
	@GetMapping("/orders/{orderId}")
	public ResponseEntity<SuccessResponse<AdminGetOrderDetailResponse>> getOrder(
			@AuthInfo JwtInfo jwtInfo,
			@PathVariable Long orderId) {
		return SuccessResponse.of(
				adminOrderApplicationService.getOrder(jwtInfo.memberId(), orderId)
		).asHttp(HttpStatus.OK);
	}

	@Override
	@PatchMapping("/orders")
	public ResponseEntity<SuccessResponse<Void>> updateOrder(
			@AuthInfo JwtInfo jwtInfo,
			@Valid @RequestBody AdminUpdateOrderRequest adminUpdateOrderRequest) {
		adminOrderApplicationService.updateOrder(jwtInfo.memberId(), adminUpdateOrderRequest);
		return SuccessResponse.ofNoData().asHttp(HttpStatus.OK);
	}

	private ResponseCookie getRefreshTokenResponseCookie(String refreshToken) {
		return ResponseCookie.from(CustomCookieName.REFRESH_TOKEN, refreshToken)
				.httpOnly(true)
				.secure(true)
				.path("/")
				.build();
	}

}

