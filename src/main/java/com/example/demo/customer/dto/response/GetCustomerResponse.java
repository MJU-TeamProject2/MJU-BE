package com.example.demo.customer.dto.response;

import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.entity.Gender;

public record GetCustomerResponse(String email, String name, String nickName, int age,
								  Gender gender, String phoneNumber, int height, int weight, String bodyType) {
	public static GetCustomerResponse from(Customer customer) {
		return new GetCustomerResponse(customer.getEmail(), customer.getName(), customer.getNickName(),
			customer.getAge(), customer.getGender(), customer.getPhoneNumber(), customer.getHeight(), customer.getWeight(), customer.getBodyType().name());
	}
}
