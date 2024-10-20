package com.example.demo.customer.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.security.TokenProvider;
import com.example.demo.common.util.auth.AuthService;
import com.example.demo.customer.dto.request.ProfileUpdateRequest;
import com.example.demo.customer.dto.response.GetCustomerResponse;
import com.example.demo.customer.dto.response.LoginResponse;
import com.example.demo.customer.entity.Customer;
import com.example.demo.common.util.auth.Auth;
import com.example.demo.customer.repository.CustomerRepository;
import com.example.demo.exception.CustomerAuthNotFoundException;
import com.example.demo.exception.CustomerErrorCode;
import com.example.demo.exception.CustomerNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
	private final CustomerRepository customerRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenProvider tokenProvider;
	private final AuthService authService;

	public void register(Customer customer) {
		Customer newCustomer = customerRepository.save(customer);
		authService.createAuth(newCustomer.getEmail());
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

		String accessToken = tokenProvider.createAccessToken(customer.getId(), customer.getRole());
		String refreshToken = tokenProvider.createRefreshToken(customer.getId(), customer.getRole());

		Auth auth = authService.findByCode(customer.getEmail());
		auth.updateRefreshToken(refreshToken);

		return LoginResponse.builder()
			.customerId(customer.getId())
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.role(customer.getRole())
			.build();
	}

	@Transactional(readOnly = true)
	public GetCustomerResponse retrieveProfile(Long customerId) {
		return GetCustomerResponse.from(customerRepository.findById(customerId).orElseThrow(
			CustomerAuthNotFoundException::new));
	}

	@Transactional
	public void updateProfile(Long customerId, ProfileUpdateRequest profileUpdateRequest) {
		Customer customer = customerRepository.getReferenceById(customerId);
		customer.update(profileUpdateRequest.email(), profileUpdateRequest.name(), profileUpdateRequest.nickName(),
			profileUpdateRequest.age(), profileUpdateRequest.phoneNumber());
	}

	public Customer findById(Long customerId) {
		return customerRepository.findById(customerId).orElseThrow(CustomerNotFoundException::new);
	}
}
