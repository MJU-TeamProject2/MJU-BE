package com.example.demo.controller;

import com.example.demo.admin.dto.request.AdminUpdateOrderRequest;
import com.example.demo.admin.dto.response.AdminGetOrderDetailResponse;
import com.example.demo.admin.dto.response.AdminGetOrderResponse;
import com.example.demo.common.security.AuthInfo;
import com.example.demo.common.security.JwtInfo;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
import org.springframework.web.bind.annotation.RequestParam;

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
	ResponseEntity<SuccessResponse<List<AdminGetOrderResponse>>> getOrders(
			@AuthInfo JwtInfo jwtInfo,
			@Parameter(
					description = "회원 ID (선택적, 입력시 해당 회원의 주문만 조회)",
					example = "123",
					schema = @Schema(type = "integer", format = "int64")
			)
			@RequestParam(required = false) Long memberId,
			@Parameter(
					description = "페이지 크기",
					example = "20",
					schema = @Schema(type = "integer", defaultValue = "20", minimum = "1")
			)
			@RequestParam(value = "size", required = false, defaultValue = "20") int size,
			@Parameter(
					description = "페이지 번호 (0부터 시작)",
					example = "0",
					schema = @Schema(type = "integer", defaultValue = "0", minimum = "0")
			)
			@RequestParam(value = "page", required = false, defaultValue = "0") int page
	);

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
