package com.example.demo.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.clothes.dto.GetClothesResponse;
import com.example.demo.clothes.service.ClothesService;
import com.example.demo.common.dto.PageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class ClothesController implements ClothesControllerInterface{

	private final ClothesService clothesService;
	@Override
	public PageResponse<GetClothesResponse> getAllClothes(
		@RequestParam(value = "size", required = false, defaultValue = "20") int size,
		@RequestParam(value = "page", required = false, defaultValue = "0") int page,
		@RequestParam(name = "sort", required = false, defaultValue = "LATEST") Sort sort) {

		return clothesService.getAllClothes(PageRequest.of(page, size, sort));
	}
}
