package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.dto.SuccessResponse;
import com.example.demo.customer.dto.request.RegisterRequest;
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
		// TODO email format 적용 & 비밀번호 암호화
		checkEmailDuplicate(registerRequest.email());
		Customer customer = Customer.builder()
			.name(registerRequest.name())
			.age(registerRequest.age())
			.gender(registerRequest.gender())
			.email(registerRequest.email())
			.password(passwordEncoder.encode(registerRequest.password()))
			.phoneNumber(registerRequest.phoneNumber())
			.deleted(false)
			.build();
		customerService.register(customer);

		return SuccessResponse.ofNoData().asHttp(HttpStatus.OK);
	}

	private void checkEmailDuplicate(String email) {
		if(customerService.checkEmailDuplicate(email)) {
			throw new RuntimeException("이미 가입한 이메일입니다.");
		}
	}
}
