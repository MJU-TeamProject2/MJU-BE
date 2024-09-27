package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.dto.SuccessResponse;
import com.example.demo.common.security.CustomCookieName;
import com.example.demo.customer.dto.request.LoginRequest;
import com.example.demo.customer.dto.request.RegisterRequest;
import com.example.demo.customer.dto.response.LoginResponse;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer")
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

	private ResponseCookie getRefreshTokenResponseCookie(String refreshToken) {
		return ResponseCookie.from(CustomCookieName.REFRESH_TOKEN, refreshToken)
			.httpOnly(true)
			.secure(true)
			.path("/")
			.build();
	}
}
