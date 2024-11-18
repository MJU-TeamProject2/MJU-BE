package com.example.demo.clothes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.example.demo.clothes.dto.response.GetClothesDetailResponse;
import com.example.demo.clothes.dto.response.GetClothesResponse;
import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.entity.ClothesCategory;
import com.example.demo.clothes.repository.ClothesRepository;
import com.example.demo.clothes.service.ClothesService;
import com.example.demo.common.dto.PageResponse;
import com.example.demo.common.util.S3Service;
import com.example.demo.exception.ClothesNotFoundException;

@ExtendWith(MockitoExtension.class)
class ClothesServiceTest {

	@InjectMocks
	private ClothesService clothesService;

	@Mock
	private ClothesRepository clothesRepository;
	@Mock
	private S3Service s3Service;

	private Clothes testClothes;

	@BeforeEach
	void setUp() {
		testClothes = Clothes.builder()
			.id(1L)
			.category(ClothesCategory.TOPS)
			.imageUrl("test-image.jpg")
			.name("Test Clothes")
			.price(10000)
			.productNumber("TEST001")
			.detailUrl("test-detail.jpg")
			.objectKey("test-object.obj")
			.mtlKey("test.mtl")
			.build();
	}

	@Test
	@DisplayName("모든 옷 목록 조회 테스트")
	void getAllClothes_Success() {
		// given
		PageRequest pageRequest = PageRequest.of(0, 10);
		List<Clothes> clothesList = List.of(testClothes);
		Page<Clothes> clothesPage = new PageImpl<>(clothesList);

		when(clothesRepository.findAllByDeletedAtIsNull(pageRequest)).thenReturn(clothesPage);
		when(s3Service.generatePresignedUrl(anyString())).thenReturn("presigned-url");

		// when
		PageResponse<GetClothesResponse> response = clothesService.getAllClothes(pageRequest);

		// then
		assertNotNull(response);
		assertEquals(1, response.total());
		assertEquals("Test Clothes", response.content().get(0).name());
	}

	@Test
	@DisplayName("옷 상세 정보 조회 테스트")
	void getClothesDetail_Success() {
		// given
		when(clothesRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(testClothes));
		when(s3Service.generatePresignedUrl(anyString())).thenReturn("presigned-url");

		// when
		GetClothesDetailResponse response = clothesService.getClothesDetail(1L);

		// then
		assertNotNull(response);
		assertEquals("Test Clothes", response.name());
		assertEquals("presigned-url", response.imageUrl());
	}

	@Test
	@DisplayName("옷 상세 정보 조회 실패 - 존재하지 않는 상품")
	void getClothesDetail_NotFound() {
		// given
		when(clothesRepository.findByIdAndDeletedAtIsNull(999L)).thenReturn(Optional.empty());

		// when & then
		assertThrows(ClothesNotFoundException.class, () -> clothesService.getClothesDetail(999L));
	}

	@Test
	@DisplayName("카테고리별 옷 목록 조회 테스트")
	void getClothesByCategory_Success() {
		// given
		PageRequest pageRequest = PageRequest.of(0, 10);
		List<Clothes> clothesList = List.of(testClothes);
		Page<Clothes> clothesPage = new PageImpl<>(clothesList);

		when(clothesRepository.findByCategoryAndDeletedAtIsNull(pageRequest, ClothesCategory.TOPS))
			.thenReturn(clothesPage);
		when(s3Service.generatePresignedUrl(anyString())).thenReturn("presigned-url");

		// when
		PageResponse<GetClothesResponse> response = clothesService.getClothesByCategory(pageRequest,
			ClothesCategory.TOPS);

		// then
		assertNotNull(response);
		assertEquals(1, response.total());
		assertEquals(ClothesCategory.TOPS, testClothes.getCategory());
		assertEquals("Test Clothes", response.content().get(0).name());
	}
}
