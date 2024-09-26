package com.example.demo.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.clothes.dto.GetClothesDetailResponse;
import com.example.demo.clothes.dto.GetClothesResponse;
import com.example.demo.clothes.service.ClothesService;
import com.example.demo.common.dto.PageResponse;
import com.example.demo.common.dto.SuccessResponse;
import com.example.demo.util.SortUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/clothes")
@RequiredArgsConstructor
public class ClothesControllerImpl implements ClothesController {

	private final ClothesService clothesService;

	@Override
	@GetMapping("/all")
	public ResponseEntity<SuccessResponse<PageResponse<GetClothesResponse>>> getAllClothes(
		@RequestParam(value = "size", required = false, defaultValue = "20") int size,
		@RequestParam(value = "page", required = false, defaultValue = "0") int page,
		@RequestParam(name = "sort", required = false, defaultValue = "LATEST") String sortOption) {

		Sort sort = SortUtils.createSort(sortOption);
		PageRequest pageRequest = PageRequest.of(page, size, sort);
		return SuccessResponse.of(clothesService.getAllClothes(pageRequest))
			.asHttp(HttpStatus.OK);
	}

	@Override
	@GetMapping("/{clothesId}")
	public ResponseEntity<SuccessResponse<GetClothesDetailResponse>> getClothesDetail(Long clothesId) {
		return null;
	}
}
