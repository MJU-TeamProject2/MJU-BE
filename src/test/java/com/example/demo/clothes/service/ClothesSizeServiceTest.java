package com.example.demo.clothes.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.entity.ClothesCategory;
import com.example.demo.clothes.entity.ClothesSize;
import com.example.demo.clothes.entity.Size;
import com.example.demo.clothes.repository.ClothesSizeRepository;
import com.example.demo.common.exception.CustomException;
import com.example.demo.util.TestResultLogger;

import lombok.extern.slf4j.Slf4j;

@ExtendWith({MockitoExtension.class, TestResultLogger.class})
@Slf4j
class ClothesSizeServiceTest {

	@Mock
	private ClothesSizeRepository clothesSizeRepository;

	private ClothesSizeService clothesSizeService;
	private Clothes testClothes;
	private ClothesSize testClothesSize;

	private final static long ID = 1L;
	private final static String IMAGE_URL = "test-image.jpg";
	private final static String NAME = "Test Clothes";
	private final static int PRICE = 10000;
	private final static String PRODUCT_NUMBER = "TEST001";
	private final static String DETAIL_URL = "test-detail.jpg";
	private final static String OBJECT_KEY = "test-object.obj";
	private final static String MTL_KEY = "test.mtl";
	private final static int QUANTITY = 10;
	private final static int SUCCESS_QUANTITY = 5;
	private final static int FAIL_QUANTITY = -1;

	@BeforeEach
	void setUp() {
		clothesSizeService = new ClothesSizeService(clothesSizeRepository);

		testClothes = Clothes.builder()
			.id(ID)
			.category(ClothesCategory.TOPS)
			.imageUrl(IMAGE_URL)
			.name(NAME)
			.price(PRICE)
			.productNumber(PRODUCT_NUMBER)
			.detailUrl(DETAIL_URL)
			.objectKey(OBJECT_KEY)
			.mtlKey(MTL_KEY)
			.build();

		testClothesSize = ClothesSize.builder()
			.clothes(testClothes)
			.size(Size.M)
			.quantity(QUANTITY)
			.build();
	}

	@Test
	@DisplayName("특정 의류와 사이즈로 ClothesSize 찾기 - 존재하는 경우")
	void 사이즈별_의상조회_성공() {
		// Given
		given(clothesSizeRepository.findByClothesAndSize(testClothes, Size.M))
			.willReturn(Optional.of(testClothesSize));

		// When
		ClothesSize result = clothesSizeService.findByClothesIdAndSize(testClothes, Size.M);

		// Then
		then(clothesSizeRepository).should().findByClothesAndSize(testClothes, Size.M);
		assertNotNull(result);
		assertEquals(testClothesSize, result);
	}

	@Test
	@DisplayName("특정 의류와 사이즈로 ClothesSize 찾기 - 존재하지 않는 경우")
	void 사이즈별_의상조회_실패() {
		// Given
		given(clothesSizeRepository.findByClothesAndSize(testClothes, Size.L))
			.willReturn(Optional.empty());

		// When & Then
		assertThrows(CustomException.class,
			() -> clothesSizeService.findByClothesIdAndSize(testClothes, Size.L));
	}

	@Test
	@DisplayName("의류 수량 업데이트 - 유효한 수량")
	void 유효한_의상수량_수정_성공() {
		// Given & When
		clothesSizeService.updateClothesQuantity(testClothesSize, SUCCESS_QUANTITY);

		// Then
		assertEquals(SUCCESS_QUANTITY, testClothesSize.getQuantity());
		then(clothesSizeRepository).should().save(testClothesSize);
	}

	@Test
	@DisplayName("의류 수량 업데이트 - 음수 수량")
	void 음수_의상수량_수정_실패() {
		// Given
		// When & Then
		assertThrows(CustomException.class,
			() -> clothesSizeService.updateClothesQuantity(testClothesSize, FAIL_QUANTITY));
	}
}
