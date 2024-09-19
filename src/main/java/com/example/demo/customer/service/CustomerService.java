package com.example.demo.customer.service;

import org.springframework.stereotype.Service;

import com.example.demo.customer.dto.GetCustomerResponse;
import com.example.demo.customer.entity.Customer;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.customer.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
	private final CustomerRepository customerRepository;

}
