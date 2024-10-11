package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.clothes.dto.GetClothesDetailResponse;
import com.example.demo.clothes.dto.GetClothesResponse;
import com.example.demo.common.dto.PageResponse;
import com.example.demo.common.dto.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Clothes API", description = "의류 API")
public interface ClothesController {

	@Operation(summary = "의류 전체 조회", description = "Page에 맞게 의류 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200"
			, description = "성공적으로 조회")
	})
	@GetMapping("/all")
	ResponseEntity<SuccessResponse<PageResponse<GetClothesResponse>>> getAllClothes(
		@RequestParam(value = "size", required = false, defaultValue = "20") int size,
		@RequestParam(value = "page", required = false, defaultValue = "0") int page,
		@RequestParam(name = "sort", required = false, defaultValue = "LATEST") String sortOptions);

	@Operation(summary = "의류 상세 조회", description = "특정 의류 상세 정보 조회")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "성공적으로 조회")
	})
	@GetMapping("/{clothesId}")
	ResponseEntity<SuccessResponse<GetClothesDetailResponse>> getClothesDetail(
		@PathVariable Long clothesId
	);

}
