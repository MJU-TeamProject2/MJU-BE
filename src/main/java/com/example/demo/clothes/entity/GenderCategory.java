package com.example.demo.clothes.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GenderCategory {
	MALE("남성용"), FEMALE("여성용"), UNISEX("공용");

	private final String name;
}
