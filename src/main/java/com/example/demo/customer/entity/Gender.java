package com.example.demo.customer.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Gender {
	M("남자"), F("여자");

	private final String name;
}
