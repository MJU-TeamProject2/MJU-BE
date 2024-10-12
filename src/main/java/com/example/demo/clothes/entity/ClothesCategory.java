package com.example.demo.clothes.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ClothesCategory {
	SHOES("SHOES"),
	TOPS("TOPS"),
	OUTERWEAR("OUTERWEAR"),
	PANTS("PANTS"),
	DRESSES("DRESSES");

	private final String name;
}
