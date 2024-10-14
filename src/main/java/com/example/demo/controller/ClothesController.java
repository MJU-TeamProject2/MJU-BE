package com.example.demo.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.clothes.dto.request.CreateClothesRequest;
import com.example.demo.clothes.dto.response.GetClothesDetailResponse;
import com.example.demo.clothes.dto.response.GetClothesResponse;
import com.example.demo.clothes.entity.ClothesCategory;
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

	@Operation(summary = "상품 정보 수정", description = "상품 정보 수정을 위한 API")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "성공적으로 상품 수정 완료")
	})
	@PatchMapping(value = "/products/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ResponseEntity<SuccessResponse<Void>> updateProduct(
		@AuthInfo JwtInfo jwtInfo, @Valid @ModelAttribute UpdateClothesRequest updateClothesRequest,
		@PathVariable Long productId);

	@Operation(summary = "상품 삭제", description = "상품 삭제를 위한 API")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "성공적으로 상품 삭제 완료")
	})
	@DeleteMapping(value = "/products/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ResponseEntity<SuccessResponse<Void>> deleteProduct(
		@AuthInfo JwtInfo jwtInfo, @PathVariable Long productId);

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

	@Operation(summary = "카테고리별 의상 조회", description = "카테고리에 따른 의상 조회")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "성공적으로 조회"
		)
	})
	@GetMapping("/by/{category}")
	ResponseEntity<SuccessResponse<PageResponse<GetClothesResponse>>> getClothesByCategory(
		@PathVariable(value = "category") ClothesCategory clothesCategory,
		@RequestParam(value = "size", required = false, defaultValue = "20") int size,
		@RequestParam(value = "page", required = false, defaultValue = "0") int page,
		@RequestParam(name = "sort", required = false, defaultValue = "LATEST") String sortOptions);
}
