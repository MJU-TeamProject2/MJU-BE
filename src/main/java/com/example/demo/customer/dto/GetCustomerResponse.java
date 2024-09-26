package com.example.demo.customer.dto;

import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.entity.Gender;

public record GetCustomerResponse(String name, int age, Gender gender) {
	public static GetCustomerResponse from(Customer customer) {
		return new GetCustomerResponse(customer.getName(), customer.getAge(), customer.getGender());
	}
}
