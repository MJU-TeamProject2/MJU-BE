package com.example.demo.util;

import org.springframework.data.domain.Page;

import com.example.demo.common.dto.PageResponse;

public class PageUtils {
	public static <T> PageResponse<T> toPageResponse(Page<T> page) {
		return new PageResponse<>(page.getNumber(), page.getSize(), page.getTotalElements(), page.getContent());
	}
}
