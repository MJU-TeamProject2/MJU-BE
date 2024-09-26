package com.example.demo.customer.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.security.TokenProvider;
import com.example.demo.customer.dto.response.LoginResponse;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.entity.CustomerAuth;
import com.example.demo.customer.repository.CustomerRepository;
import com.example.demo.exception.CustomerErrorCode;
import com.example.demo.exception.CustomerNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
	private final CustomerRepository customerRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenProvider tokenProvider;
	private final CustomerAuthService customerAuthService;

	public void register(Customer customer) {
		Customer newCustomer = customerRepository.save(customer);
		customerAuthService.createCustomerAuth(newCustomer.getId());
	}

	public void checkEmailDuplicate(String email) {
		if(customerRepository.findByEmail(email).isPresent()) {
			throw new CustomException(CustomerErrorCode.CUSTOMER_EMAIL_DUPLICATE);
		}
	}

	@Transactional
	public LoginResponse login(String email, String password) {
		Customer customer = customerRepository.findByEmail(email)
			.orElseThrow(CustomerNotFoundException::new);
		if (!passwordEncoder.matches(password, customer.getPassword())) {
			throw new CustomException(CustomerErrorCode.CUSTOMER_WRONG_PASSWORD);
		}

		String accessToken = tokenProvider.createAccessToken(customer.getId());
		String refreshToken = tokenProvider.createRefreshToken(customer.getId());

		CustomerAuth customerAuth = customerAuthService.findByCustomerId(customer.getId());
		customerAuth.updateRefreshToken(refreshToken);

		return LoginResponse.builder()
			.customerId(customer.getId())
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
