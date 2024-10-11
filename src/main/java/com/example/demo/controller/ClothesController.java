package com.example.demo.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.clothes.dto.request.CreateClothesRequest;
import com.example.demo.clothes.dto.response.GetClothesDetailResponse;
import com.example.demo.clothes.dto.response.GetClothesResponse;
import com.example.demo.common.dto.PageResponse;
import com.example.demo.common.dto.SuccessResponse;
import com.example.demo.common.security.AuthInfo;
import com.example.demo.common.security.JwtInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Clothes API", description = "의류 API")
public interface ClothesController {

	@Operation(summary = "상품 등록", description = "상품 등록을 위한 API")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "성공적으로 상품 등록 완료")
	})
	@PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ResponseEntity<SuccessResponse<Void>> createProduct(
		@AuthInfo JwtInfo jwtInfo, @Valid @ModelAttribute CreateClothesRequest createClothesRequest);

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

	@Operation(summary = "의류 Obj 파일 다운", description = "특정 의류 Obj 파일 바로 다운")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "성공적으로 조회")
	})
	@GetMapping("/download/{clothesId}")
	ResponseEntity<byte[]> getClothesObject(
		@PathVariable Long clothesId
	);
}
