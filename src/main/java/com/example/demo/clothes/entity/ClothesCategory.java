package com.example.demo.clothes.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ClothesCategory {
	SHOES("신발"),
	TOPS("상의"),
	OUTERWEAR("아우터"),
	PANTS("하의"),
	DRESSES("드레스");

	private final String name;
}
