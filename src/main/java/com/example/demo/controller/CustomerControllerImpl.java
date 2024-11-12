package com.example.demo.controller;

import com.example.demo.customer.dto.request.AddAddressRequest;
import com.example.demo.customer.dto.request.AddPaymentRequest;
import com.example.demo.customer.dto.request.UpdateAddressRequest;
import com.example.demo.customer.dto.request.UpdatePaymentRequest;
import com.example.demo.customer.dto.response.GetAddressDetailResponse;
import com.example.demo.customer.dto.response.GetAddressResponse;
import com.example.demo.customer.dto.response.GetPaymentDetailResponse;
import com.example.demo.customer.dto.response.GetPaymentResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.dto.SuccessResponse;
import com.example.demo.common.security.AuthInfo;
import com.example.demo.common.security.CustomCookieName;
import com.example.demo.common.security.JwtInfo;
import com.example.demo.customer.dto.request.ProfileUpdateRequest;
import com.example.demo.customer.dto.response.GetCustomerResponse;
import com.example.demo.customer.dto.request.LoginRequest;
import com.example.demo.customer.dto.request.RegisterRequest;
import com.example.demo.customer.dto.response.LoginResponse;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerControllerImpl implements CustomerController {

	private final CustomerService customerService;
	private final PasswordEncoder passwordEncoder;

	@Override
	@PostMapping("/register")
	public ResponseEntity<SuccessResponse<Void>> register(@Valid @RequestBody RegisterRequest registerRequest) {
		customerService.checkEmailDuplicate(registerRequest.email());
		Customer customer = Customer.builder()
			.name(registerRequest.name())
			.nickName(registerRequest.nickName())
			.age(registerRequest.age())
			.gender(registerRequest.gender())
			.email(registerRequest.email())
			.password(passwordEncoder.encode(registerRequest.password()))
			.phoneNumber(registerRequest.phoneNumber())
			.height(registerRequest.height())
			.weight(registerRequest.weight())
			.bodyType(registerRequest.bodyType())
			.build();
		customerService.register(customer);

		return SuccessResponse.ofNoData().asHttp(HttpStatus.OK);
	}

	@Override
	@PostMapping(value = "/login")
	public ResponseEntity<SuccessResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
		LoginResponse loginResponse = customerService.login(loginRequest.email(), loginRequest.password());
		var refreshTokenResponseCookie = getRefreshTokenResponseCookie(loginResponse.refreshToken());
		return SuccessResponse.of(loginResponse).okWithCookie(refreshTokenResponseCookie);
	}

	@Override
	public ResponseEntity<SuccessResponse<GetCustomerResponse>> retrieveProfile(@AuthInfo JwtInfo jwtInfo) {
		System.out.println(jwtInfo + "  " + jwtInfo.memberId());
		GetCustomerResponse customerResponse = customerService.retrieveProfile(jwtInfo.memberId());
		return SuccessResponse.of(customerResponse).asHttp(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<SuccessResponse<Void>> updateProfile(JwtInfo jwtInfo,
		ProfileUpdateRequest profileUpdateRequest) {
		customerService.updateProfile(jwtInfo.memberId(), profileUpdateRequest);
		return SuccessResponse.ofNoData().asHttp(HttpStatus.OK);
	}

	@Override
	@GetMapping("/address")
	public ResponseEntity<SuccessResponse<List<GetAddressResponse>>> getAddresses(@AuthInfo JwtInfo jwtInfo) {
		return SuccessResponse.of(
				customerService.getAddresses(jwtInfo.memberId())
		).asHttp(HttpStatus.OK);
	}

	@Override
	@GetMapping("/address/{addressId}")
	public ResponseEntity<SuccessResponse<GetAddressDetailResponse>> getAddressDetail(@AuthInfo JwtInfo jwtInfo,
			@PathVariable Long addressId) {
		return SuccessResponse.of(
				customerService.getAddressDetail(jwtInfo.memberId(), addressId)
		).asHttp(HttpStatus.OK);
	}

	@Override
	@PostMapping("/address")
	public ResponseEntity<SuccessResponse<Void>> addAddress(@AuthInfo JwtInfo jwtInfo,
			@Valid @RequestBody AddAddressRequest addAddressRequest) {
		customerService.addAddress(jwtInfo.memberId(), addAddressRequest);
		return SuccessResponse.ofNoData().asHttp(HttpStatus.OK);
	}

	@Override
	@PatchMapping("/address")
	public ResponseEntity<SuccessResponse<Void>> updateAddress(@AuthInfo JwtInfo jwtInfo,
			@Valid @RequestBody UpdateAddressRequest updateAddressRequest) {
		customerService.updateAddress(jwtInfo.memberId(), updateAddressRequest);
		return SuccessResponse.ofNoData().asHttp(HttpStatus.OK);
	}

	@Override
	@DeleteMapping("/address/{addressId}")
	public ResponseEntity<SuccessResponse<Void>> deleteAddress(@AuthInfo JwtInfo jwtInfo, @PathVariable Long addressId) {
		customerService.deleteAddress(jwtInfo.memberId(), addressId);
		return SuccessResponse.ofNoData().asHttp(HttpStatus.OK);
	}

	@Override
	@GetMapping("/payment")
	public ResponseEntity<SuccessResponse<List<GetPaymentResponse>>> getPayments(@AuthInfo JwtInfo jwtInfo) {
		return SuccessResponse.of(
				customerService.getPayments(jwtInfo.memberId())
		).asHttp(HttpStatus.OK);
	}

	@Override
	@GetMapping("/payment/{paymentId}")
	public ResponseEntity<SuccessResponse<GetPaymentDetailResponse>> getPaymentDetail(@AuthInfo JwtInfo jwtInfo,
		@PathVariable	Long paymentId) {
		return SuccessResponse.of(
				customerService.getPaymentDetail(jwtInfo.memberId(), paymentId)
		).asHttp(HttpStatus.OK);
	}

	@Override
	@PostMapping("/payment")
	public ResponseEntity<SuccessResponse<Void>> addPayment(@AuthInfo JwtInfo jwtInfo,
			@Valid @RequestBody AddPaymentRequest addPaymentRequest) {
		customerService.addPayment(jwtInfo.memberId(), addPaymentRequest);
		return SuccessResponse.ofNoData().asHttp(HttpStatus.OK);
	}

	@Override
	@PatchMapping("/payment")
	public ResponseEntity<SuccessResponse<Void>> updatePayment(@AuthInfo JwtInfo jwtInfo,
			@Valid @RequestBody UpdatePaymentRequest updatePaymentRequest) {
		customerService.updatePayment(jwtInfo.memberId(), updatePaymentRequest);
		return SuccessResponse.ofNoData().asHttp(HttpStatus.OK);
	}

	@Override
	@DeleteMapping("/payment/{paymentId}")
	public ResponseEntity<SuccessResponse<Void>> deletePayment(@AuthInfo JwtInfo jwtInfo, @PathVariable Long paymentId) {
		customerService.deletePayment(jwtInfo.memberId(), paymentId);
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
