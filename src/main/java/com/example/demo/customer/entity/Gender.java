package com.example.demo.customer.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Gender {
	M("남자"), F("여자");

	private final String name;

	@JsonCreator
	public static Gender fromString(String input) {
		for(Gender gender : Gender.values()) {
			if (gender.name.equalsIgnoreCase(input) || gender.name().equalsIgnoreCase(input)) {
				return gender;
			}
		}
		throw new IllegalArgumentException("No constant with text " + input + " found");
	}
}
