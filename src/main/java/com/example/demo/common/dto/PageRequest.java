package com.example.demo.common.dto;

import java.util.List;

public record PageRequest(int page, int size, List<Enum<?>> sort) {

	public PageRequest {
		if (page < 0 || size < 1) {
			String errorMessage = String.format("Invalid PageRequest: page=%d, size=%d,", page, size);
			throw new IllegalArgumentException(errorMessage);
		}
	}

	public static PageRequest of(int page, int size, Enum<?>... sort) {
		return new PageRequest(page, size, List.of(sort));
	}

}
