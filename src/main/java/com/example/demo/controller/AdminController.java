package com.example.demo.controller;

import com.example.demo.admin.dto.request.AdminUpdateOrderRequest;
import com.example.demo.admin.dto.response.AdminGetOrderDetailResponse;
import com.example.demo.admin.dto.response.AdminGetOrderResponse;
import com.example.demo.common.security.AuthInfo;
import com.example.demo.common.security.JwtInfo;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.admin.dto.request.AdminLoginRequest;
import com.example.demo.admin.dto.response.AdminLoginResponse;
import com.example.demo.common.dto.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

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

	@Operation(summary = "관리자 주문 목록 조회", description = "모든 사용자의 주문 목록을 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "성공적으로 조회 완료"
			)
	})
	@GetMapping("/orders")
	ResponseEntity<SuccessResponse<List<AdminGetOrderResponse>>> getOrders(@AuthInfo JwtInfo jwtInfo);

	@Operation(summary = "관리자 주문 상세 조회", description = "특정 주문의 상세 정보를 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "주문 상세를 성공적으로 조회 완료"
			)
	})
	@GetMapping("/orders/{orderId}")
	ResponseEntity<SuccessResponse<AdminGetOrderDetailResponse>> getOrder(
			@AuthInfo JwtInfo jwtInfo, @PathVariable Long orderId
	);

	@Operation(summary = "관리자 주문 상태 수정", description = "주문의 상태를 수정합니다.")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "주문이 성공적으로 수정 완료"
			),
	})
	@PatchMapping("/orders")
	ResponseEntity<SuccessResponse<Void>> updateOrder(
			@AuthInfo JwtInfo jwtInfo,
			@Valid @RequestBody AdminUpdateOrderRequest adminUpdateOrderRequest
	);
}
