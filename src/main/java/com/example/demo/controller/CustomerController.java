package com.example.demo.controller;

import com.example.demo.customer.dto.request.AddAddressRequest;
import com.example.demo.customer.dto.request.AddPaymentRequest;
import com.example.demo.customer.dto.request.UpdateAddressRequest;
import com.example.demo.customer.dto.request.UpdatePaymentRequest;
import com.example.demo.customer.dto.response.GetAddressDetailResponse;
import com.example.demo.customer.dto.response.GetAddressResponse;
import com.example.demo.customer.dto.response.GetPaymentDetailResponse;
import com.example.demo.customer.dto.response.GetPaymentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.common.dto.SuccessResponse;
import com.example.demo.common.security.AuthInfo;
import com.example.demo.common.security.JwtInfo;
import com.example.demo.customer.dto.request.LoginRequest;
import com.example.demo.customer.dto.request.ProfileUpdateRequest;
import com.example.demo.customer.dto.request.RegisterRequest;
import com.example.demo.customer.dto.response.GetCustomerResponse;
import com.example.demo.customer.dto.response.LoginResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

@Tag(name = "Customer API", description = "Customer API")
public interface CustomerController {

	@Operation(summary = "고객 회원가입 API", description = "고객의 회원가입을 위한 API")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "성공적으로 메시지 반환")

	})
	@PostMapping(value = "/register")
	ResponseEntity<SuccessResponse<Void>> register(@RequestBody RegisterRequest registerRequest);

	@Operation(summary = "고객 로그인 API", description = "고객의 로그인을 위한 API")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "성공적으로 로그인 완료")

	})
	@PostMapping(value = "/login")
	ResponseEntity<SuccessResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest);

	@Operation(summary = "내 정보 조회 API", description = "마이페이지에서 내 정보 조회를 위한 API")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "성공적으로 조회 완료")
	})
	@GetMapping(value = "/profile")
	ResponseEntity<SuccessResponse<GetCustomerResponse>> retrieveProfile(@AuthInfo JwtInfo jwtInfo);

	@Operation(summary = "내 정보 수정 API", description = "마이페이지에서 내 정보 수정을 위한 API")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "성공적으로 수정 완료")
	})
	@PatchMapping(value = "/profile")
	ResponseEntity<SuccessResponse<Void>> updateProfile(@AuthInfo JwtInfo jwtInfo,
		@Valid @RequestBody ProfileUpdateRequest profileUpdateRequest);

	@Operation(summary = "내 주소 조회 API", description = "내가 등록한 주소를 가져오는 API")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "성공적으로 조회 완료")
	})
	@GetMapping("/address")
	ResponseEntity<SuccessResponse<List<GetAddressResponse>>> getAddresses(@AuthInfo JwtInfo jwtInfo);

	@Operation(summary = "내 주소 상세 조회 API", description = "내가 등록한 상세 주소를 가져오는 API")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "성공적으로 조회 완료")
	})
	@GetMapping("/address/{addressId}")
	ResponseEntity<SuccessResponse<GetAddressDetailResponse>> getAddressDetail(@AuthInfo JwtInfo jwtInfo, @PathVariable Long addressId);

	@Operation(summary = "내 주소 생성 API", description = "내가 주소를 생성 하는 API")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "성공적으로 생성 완료")
	})
	@PostMapping("/address")
	ResponseEntity<SuccessResponse<Void>> addAddress(@AuthInfo JwtInfo jwtInfo, @Valid @RequestBody AddAddressRequest addAddressRequest);


	@Operation(summary = "내 주소 수정 API", description = "내가 주소를 수정 하는 API")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "성공적으로 수정 완료")
	})
	@PatchMapping("/address")
	ResponseEntity<SuccessResponse<Void>> updateAddress(@AuthInfo JwtInfo jwtInfo, @Valid @RequestBody UpdateAddressRequest updateAddressRequest);

	@Operation(summary = "내 주소 삭제 API", description = "내가 주소를 삭제 하는 API")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "성공적으로 삭제 완료")
	})
	@DeleteMapping("/address/{addressId}")
	ResponseEntity<SuccessResponse<Void>> deleteAddress(@AuthInfo JwtInfo jwtInfo, @PathVariable Long addressId);

	@Operation(summary = "내 결제 수단 조회 API", description = "내가 등록한 결제 수단을 가져오는 API")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "성공적으로 조회 완료")
	})
	@GetMapping("/payment")
	ResponseEntity<SuccessResponse<List<GetPaymentResponse>>> getPayments(@AuthInfo JwtInfo jwtInfo);

	@Operation(summary = "내 결제 수단 상세 조회 API", description = "내가 등록한 결제 수단의 상세 정보를 가져오는 API")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "성공적으로 조회 완료")
	})
	@GetMapping("/payment/{paymentId}")
	ResponseEntity<SuccessResponse<GetPaymentDetailResponse>> getPaymentDetail(@AuthInfo JwtInfo jwtInfo, @PathVariable Long paymentId);

	@Operation(summary = "내 결제 수단 등록 API", description = "내가 결제 수단을 등록하는 API")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "성공적으로 등록 완료")
	})
	@PostMapping("/payment")
	ResponseEntity<SuccessResponse<Void>> addPayment(@AuthInfo JwtInfo jwtInfo, @Valid @RequestBody AddPaymentRequest addPaymentRequest);

	@Operation(summary = "내 결제 수단 수정 API", description = "내가 결제 수단을 수정하는 API")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "성공적으로 수정 완료")
	})
	@PatchMapping("/payment")
	ResponseEntity<SuccessResponse<Void>> updatePayment(@AuthInfo JwtInfo jwtInfo, @Valid @RequestBody UpdatePaymentRequest updatePaymentRequest);

	@Operation(summary = "내 결제 수단 삭제 API", description = "내가 결제 수단을 삭제하는 API")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "성공적으로 삭제 완료")
	})
	@DeleteMapping("/payment/{paymentId}")
	ResponseEntity<SuccessResponse<Void>> deletePayment(@AuthInfo JwtInfo jwtInfo, @PathVariable Long paymentId);
}
