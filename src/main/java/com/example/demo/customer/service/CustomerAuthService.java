package com.example.demo.customer.service;

import org.springframework.stereotype.Service;

import com.example.demo.customer.entity.CustomerAuth;
import com.example.demo.customer.repository.CustomerAuthRepository;
import com.example.demo.exception.CustomerAuthNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerAuthService {

	private final CustomerAuthRepository customerAuthRepository;

	public void createCustomerAuth(Long customerId) {
		customerAuthRepository.save(CustomerAuth.createAuth(customerId));
	}

	public CustomerAuth findByCustomerId(Long customerId) {
		return customerAuthRepository.findByCustomerId(customerId)
			.orElseThrow(CustomerAuthNotFoundException::new);
	}
}
