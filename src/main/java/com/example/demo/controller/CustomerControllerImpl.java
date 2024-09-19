package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.customer.dto.request.RegisterRequest;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerControllerImpl implements CustomerController {

	private final CustomerService customerService;

	@Override
	@PostMapping("/register")
	public void register(@RequestBody RegisterRequest registerRequest) {
		// TODO email format 적용 & 비밀번호 암호화
		checkEmailDuplicate(registerRequest.email());
		Customer customer = Customer.builder()
			.name(registerRequest.name())
			.age(registerRequest.age())
			.gender(registerRequest.gender())
			.email(registerRequest.email())
			.password(registerRequest.password())
			.phoneNumber(registerRequest.phoneNumber())
			.deleted(false)
			.build();
		customerService.register(customer);
	}

	private void checkEmailDuplicate(String email) {
		if(customerService.checkEmailDuplicate(email)) {
			throw new RuntimeException("이미 가입한 이메일입니다.");
		}
	}
}
