package com.example.demo.util;

import org.springframework.data.domain.Sort;

public class SortUtils {

	public static Sort createSort(String sortOption) {
		switch (sortOption.toUpperCase()) {
			case "LATEST":
				return Sort.by(Sort.Direction.DESC, "createdAt");
			case "OLDEST":
				return Sort.by(Sort.Direction.ASC, "createdAt");
			case "PRICE_HIGH":
				return Sort.by(Sort.Direction.DESC, "price");
			case "PRICE_LOW":
				return Sort.by(Sort.Direction.ASC, "price");
			default:
				return Sort.by(Sort.Direction.DESC, "createdAt"); // 기본값: 최신 순
		}
	}

}
