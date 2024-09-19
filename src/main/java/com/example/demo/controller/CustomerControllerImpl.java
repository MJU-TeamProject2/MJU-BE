package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.customer.dto.GetCustomerResponse;
import com.example.demo.common.dto.SuccessResponse;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerControllerImpl implements CustomerController {

	private CustomerService customerService;

	@Override
	public void register() {

	}
}
