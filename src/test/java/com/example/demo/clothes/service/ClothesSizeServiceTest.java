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
	// private MultipartFile testFile;

	private final static long ID = 1L;
	private final static String IMAGE_URL = "test-image.jpg";
	private final static String NAME = "Test Clothes";
	private final static int PRICE = 10000;
	private final static String PRODUCT_NUMBER = "TEST001";
	private final static String DETAIL_URL = "test-detail.jpg";
	private final static String OBJECT_KEY = "test-object.obj";
	private final static String MTL_KEY = "test.mtl";
	private final static int QUANTITY = 10;

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

		// testFile = new MockMultipartFile(
		// 	"test-file",
		// 	"test.jpg",
		// 	"image/jpeg",
		// 	"test image content".getBytes()
		// );
	}

	@Test
	@DisplayName("특정 의류와 사이즈로 ClothesSize 찾기 - 존재하는 경우")
	void 사이즈별_의상_조회_성공() {
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
	void 사이즈별_의상_조회_실패() {
		// Given
		given(clothesSizeRepository.findByClothesAndSize(testClothes, Size.L))
			.willReturn(Optional.empty());

		// When & Then
		assertThrows(CustomException.class,
			() -> clothesSizeService.findByClothesIdAndSize(testClothes, Size.L));
	}
	//
	// @Test
	// @DisplayName("의류 수량 업데이트 - 유효한 수량")
	// void updateClothesQuantity_WithValidQuantity_ShouldUpdateSuccessfully() {
	// 	// Given: 의류 사이즈와 새로운 수량 준비
	// 	Clothes clothes = new Clothes();
	// 	Size size = Size.MEDIUM;
	// 	int initialQuantity = 10;
	// 	int newQuantity = 5;
	// 	ClothesSize clothesSize = new ClothesSize(clothes, size, initialQuantity);
	//
	// 	// When: 수량 업데이트 메서드 호출
	// 	clothesSizeService.updateClothesQuantity(clothesSize, newQuantity);
	//
	// 	// Then: 결과 검증
	// 	assertEquals(newQuantity, clothesSize.getQuantity());
	// 	then(clothesSizeRepository).should().save(clothesSize);
	// }
	//
	// @Test
	// @DisplayName("의류 수량 업데이트 - 음수 수량")
	// void updateClothesQuantity_WithNegativeQuantity_ShouldThrowException() {
	// 	// Given: 의류 사이즈와 음수 수량 준비
	// 	Clothes clothes = new Clothes();
	// 	Size size = Size.MEDIUM;
	// 	int initialQuantity = 10;
	// 	int negativeQuantity = -1;
	// 	ClothesSize clothesSize = new ClothesSize(clothes, size, initialQuantity);
	//
	// 	// When & Then: 예외 발생 검증
	// 	assertThrows(ClothesInsufficientStockException.class,
	// 		() -> clothesSizeService.updateClothesQuantity(clothesSize, negativeQuantity));
	// }
}
