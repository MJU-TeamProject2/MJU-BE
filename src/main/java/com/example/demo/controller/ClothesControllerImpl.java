package com.example.demo.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.clothes.dto.request.CreateClothesRequest;
import com.example.demo.clothes.dto.request.UpdateClothesRequest;
import com.example.demo.clothes.dto.response.GetClothesDetailResponse;
import com.example.demo.clothes.dto.response.GetClothesObject;
import com.example.demo.clothes.dto.response.GetClothesResponse;
import com.example.demo.clothes.entity.ClothesCategory;
import com.example.demo.clothes.service.ClothesService;
import com.example.demo.common.dto.PageResponse;
import com.example.demo.common.dto.SuccessResponse;
import com.example.demo.common.security.AuthInfo;
import com.example.demo.common.security.JwtInfo;
import com.example.demo.util.SortUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/clothes")
@RequiredArgsConstructor
public class ClothesControllerImpl implements ClothesController {

	private final ClothesService clothesService;

	@Override
	@PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<SuccessResponse<Void>> createProduct(@AuthInfo JwtInfo jwtInfo,
		CreateClothesRequest createClothesRequest) {
		clothesService.createProduct(createClothesRequest);
		return SuccessResponse.ofNoData().asHttp(HttpStatus.OK);
	}

	@Override
	@PatchMapping(value = "/{clothesId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<SuccessResponse<Void>> updateClothes(@AuthInfo JwtInfo jwtInfo,
		@ModelAttribute UpdateClothesRequest updateClothesRequest,
		@PathVariable Long clothesId) {
		clothesService.updateProduct(clothesId, updateClothesRequest);
		return SuccessResponse.ofNoData().asHttp(HttpStatus.OK);
	}

	@Override
	@DeleteMapping(value = "/{clothesId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<SuccessResponse<Void>> deleteClothes(@AuthInfo JwtInfo jwtInfo,
		@PathVariable Long clothesId) {
		clothesService.deleteProduct(clothesId);
		return SuccessResponse.ofNoData().asHttp(HttpStatus.OK);
	}

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
	public ResponseEntity<SuccessResponse<GetClothesDetailResponse>> getClothesDetail(@PathVariable Long clothesId) {
		GetClothesDetailResponse getClothesDetailResponse = clothesService.getClothesDetail(clothesId);
		return SuccessResponse.of(getClothesDetailResponse).asHttp(HttpStatus.OK);
	}

	@Override
	@GetMapping("/download/{clothesId}")
	public ResponseEntity<byte[]> getClothesObject(@PathVariable Long clothesId) {
		GetClothesObject getClothesObject = clothesService.getClothesObject(clothesId);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDisposition(ContentDisposition.builder("attachment")
			.filename(getClothesObject.fileName())
			.build());

		return ResponseEntity.ok()
			.headers(headers)
			.contentLength(getClothesObject.file().length)
			.body(getClothesObject.file());
	}

	@Override
	public ResponseEntity<SuccessResponse<PageResponse<GetClothesResponse>>> getClothesByCategory(
		ClothesCategory clothesCategory, int size, int page, String sortOption) {
		Sort sort = SortUtils.createSort(sortOption);
		PageRequest pageRequest = PageRequest.of(page, size, sort);
		return SuccessResponse.of(clothesService.getClothesByCategory(pageRequest, clothesCategory))
			.asHttp(HttpStatus.OK);
	}
}
