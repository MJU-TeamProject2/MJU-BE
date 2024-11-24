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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.clothes.dto.request.CreateClothesRequest;
import com.example.demo.clothes.dto.request.UpdateClothesRequest;
import com.example.demo.clothes.dto.response.GetClothesDetailResponse;
import com.example.demo.clothes.dto.response.GetClothesResponse;
import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.entity.ClothesCategory;
import com.example.demo.clothes.entity.ClothesSize;
import com.example.demo.clothes.entity.GenderCategory;
import com.example.demo.clothes.entity.Size;
import com.example.demo.clothes.repository.ClothesRepository;
import com.example.demo.clothes.repository.ClothesSizeRepository;
import com.example.demo.clothes.service.ClothesService;
import com.example.demo.common.dto.PageResponse;
import com.example.demo.common.util.S3Service;
import com.example.demo.exception.ClothesNotFoundException;
import com.example.demo.util.TestResultLogger;

@ExtendWith({MockitoExtension.class, TestResultLogger.class})
class ClothesServiceTest {
	// 테스트용 상수 정의
	private static final Long TEST_CLOTHES_ID = 1L;
	private static final Long INVALID_CLOTHES_ID = 999L;
	private static final int TEST_CLOTHES_PRICE = 10000;
	private static final int UPDATED_CLOTHES_PRICE = 15000;
	private static final int TEST_QUANTITY = 10;
	private static final int UPDATED_QUANTITY = 20;
	private static final String TEST_PRODUCT_NUMBER = "TEST001";
	private static final String UPDATED_PRODUCT_NUMBER = "TEST002";
	private static final String TEST_CLOTHES_NAME = "Test Clothes";
	private static final String UPDATED_CLOTHES_NAME = "Updated Test Clothes";

	// 페이징 관련 상수
	private static final int PAGE_NUMBER = 0;
	private static final int PAGE_SIZE = 10;

	// 파일 관련 상수
	private static final String TEST_FILE_NAME = "test-file";
	private static final String TEST_FILE_ORIGINAL_NAME = "test.jpg";
	private static final String TEST_FILE_CONTENT_TYPE = "image/jpeg";
	private static final String TEST_IMAGE_CONTENT = "test image content";
	private static final String TEST_OBJECT_CONTENT = "test content";
	private static final String TEST_MTL_CONTENT = "test mtl content";

	// URL 관련 상수
	private static final String TEST_IMAGE_URL = "test-image.jpg";
	private static final String TEST_DETAIL_URL = "test-detail.jpg";
	private static final String TEST_OBJECT_KEY = "test-object.obj";
	private static final String TEST_MTL_KEY = "test.mtl";
	private static final String PRESIGNED_URL = "presigned-url";
	private static final String UPLOADED_FILE_URL = "uploaded-file-url";
	private static final String UPDATED_FILE_URL = "updated-file-url";

	@InjectMocks
	private ClothesService clothesService;

	@Mock
	private ClothesRepository clothesRepository;
	@Mock
	private ClothesSizeRepository clothesSizeRepository;
	@Mock
	private S3Service s3Service;

	private Clothes testClothes;
	private ClothesSize testClothesSize;
	private MultipartFile testFile;

	@BeforeEach
	void setUp() {
		testClothes = Clothes.builder()
			.id(TEST_CLOTHES_ID)
			.category(ClothesCategory.TOPS)
			.imageUrl(TEST_IMAGE_URL)
			.name(TEST_CLOTHES_NAME)
			.price(TEST_CLOTHES_PRICE)
			.productNumber(TEST_PRODUCT_NUMBER)
			.detailUrl(TEST_DETAIL_URL)
			.objectKey(TEST_OBJECT_KEY)
			.mtlKey(TEST_MTL_KEY)
			.build();

		testClothesSize = ClothesSize.builder()
			.clothes(testClothes)
			.size(Size.M)
			.quantity(TEST_QUANTITY)
			.build();

		testFile = new MockMultipartFile(
			TEST_FILE_NAME,
			TEST_FILE_ORIGINAL_NAME,
			TEST_FILE_CONTENT_TYPE,
			TEST_IMAGE_CONTENT.getBytes()
		);
	}

	@Test
	@DisplayName("모든 옷 목록 조회 테스트")
	void 모든_옷_조회_성공() {
		// given
		PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
		List<Clothes> clothesList = List.of(testClothes);
		Page<Clothes> clothesPage = new PageImpl<>(clothesList);

		when(clothesRepository.findAllByDeletedAtIsNull(pageRequest)).thenReturn(clothesPage);
		when(s3Service.generatePresignedUrl(anyString())).thenReturn(PRESIGNED_URL);

		// when
		PageResponse<GetClothesResponse> response = clothesService.getAllClothes(pageRequest);

		// then
		assertNotNull(response);
		assertEquals(1, response.total());
		assertEquals(TEST_CLOTHES_NAME, response.content().get(0).name());
	}

	@Test
	@DisplayName("옷 상세 정보 조회 테스트")
	void 옷_상세_조회_성공() {
		// given
		when(clothesRepository.findByIdAndDeletedAtIsNull(TEST_CLOTHES_ID))
			.thenReturn(Optional.of(testClothes));
		when(s3Service.generatePresignedUrl(anyString())).thenReturn(PRESIGNED_URL);

		// when
		GetClothesDetailResponse response = clothesService.getClothesDetail(TEST_CLOTHES_ID);

		// then
		assertNotNull(response);
		assertEquals(TEST_CLOTHES_NAME, response.name());
		assertEquals(PRESIGNED_URL, response.imageUrl());
	}

	@Test
	@DisplayName("옷 상세 정보 조회 실패 - 존재하지 않는 상품")
	void 옷_상세_정보_조회_실패() {
		// given
		when(clothesRepository.findByIdAndDeletedAtIsNull(INVALID_CLOTHES_ID))
			.thenReturn(Optional.empty());

		// when & then
		assertThrows(ClothesNotFoundException.class,
			() -> clothesService.getClothesDetail(INVALID_CLOTHES_ID));
	}

	@Test
	@DisplayName("카테고리별 옷 목록 조회 테스트")
	void 카테고리별_옷_목록_조회_성공() {
		// given
		PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
		List<Clothes> clothesList = List.of(testClothes);
		Page<Clothes> clothesPage = new PageImpl<>(clothesList);

		when(clothesRepository.findByCategoryAndDeletedAtIsNull(pageRequest, ClothesCategory.TOPS))
			.thenReturn(clothesPage);
		when(s3Service.generatePresignedUrl(anyString())).thenReturn(PRESIGNED_URL);

		// when
		PageResponse<GetClothesResponse> response = clothesService.getClothesByCategory(pageRequest,
			ClothesCategory.TOPS);

		// then
		assertNotNull(response);
		assertEquals(1, response.total());
		assertEquals(ClothesCategory.TOPS, testClothes.getCategory());
		assertEquals(TEST_CLOTHES_NAME, response.content().get(0).name());
	}

	@Test
	@DisplayName("상품 생성 테스트")
	void 상품_생성_성공() {
		// given
		CreateClothesRequest request = new CreateClothesRequest(
			ClothesCategory.TOPS,
			TEST_CLOTHES_NAME,
			TEST_CLOTHES_PRICE,
			GenderCategory.MALE,
			TEST_PRODUCT_NUMBER,
			0,
			Size.M,
			TEST_QUANTITY,
			testFile,
			testFile,
			testFile,
			testFile,
			testFile
		);

		when(s3Service.uploadFile(any(MultipartFile.class), anyString())).thenReturn(UPLOADED_FILE_URL);
		when(clothesRepository.save(any(Clothes.class))).thenReturn(testClothes);

		// when
		assertDoesNotThrow(() -> clothesService.createProduct(request));

		// then
		verify(clothesRepository).save(any(Clothes.class));
		verify(clothesSizeRepository).save(any(ClothesSize.class));
	}

	@Test
	@DisplayName("상품 업데이트 테스트")
	void 상품_업데이트_성공() {
		// given
		UpdateClothesRequest request = new UpdateClothesRequest(
			ClothesCategory.TOPS,
			UPDATED_CLOTHES_NAME,
			UPDATED_CLOTHES_PRICE,
			GenderCategory.UNISEX,
			UPDATED_PRODUCT_NUMBER,
			TEST_QUANTITY,
			Size.M,
			UPDATED_QUANTITY,
			testFile,
			testFile,
			testFile,
			testFile,
			testFile
		);

		when(clothesRepository.findByIdAndDeletedAtIsNull(TEST_CLOTHES_ID)).thenReturn(Optional.of(testClothes));
		when(clothesSizeRepository.findByClothesAndSize(any(Clothes.class), any(Size.class)))
			.thenReturn(Optional.of(testClothesSize));
		when(s3Service.uploadFile(any(MultipartFile.class), anyString())).thenReturn(UPDATED_FILE_URL);

		// when
		assertDoesNotThrow(() -> clothesService.updateProduct(TEST_CLOTHES_ID, request));

		// then
		verify(s3Service, times(5)).uploadFile(any(MultipartFile.class), anyString());
		verify(clothesSizeRepository).findByClothesAndSize(any(Clothes.class), eq(Size.M));
	}

	@Test
	@DisplayName("상품 삭제 테스트")
	void 상품_삭제_성공() {
		// given
		when(clothesRepository.findByIdAndDeletedAtIsNull(TEST_CLOTHES_ID))
			.thenReturn(Optional.of(testClothes));

		// when
		clothesService.deleteProduct(TEST_CLOTHES_ID);

		// then
		verify(s3Service).fileDeletes(anyList());
		assertNotNull(testClothes.getDeletedAt());
	}

	@Test
	@DisplayName("Object 파일 조회 테스트")
	void obj_파일_조회_성공() {
		// given
		when(clothesRepository.findByIdAndDeletedAtIsNull(TEST_CLOTHES_ID))
			.thenReturn(Optional.of(testClothes));
		when(s3Service.getObject(anyString())).thenReturn(TEST_OBJECT_CONTENT.getBytes());

		// when
		var response = clothesService.getClothesObject(TEST_CLOTHES_ID);

		// then
		assertNotNull(response);
		assertEquals(TEST_CLOTHES_ID, response.clothesId());
		assertNotNull(response.file());
	}

	@Test
	@DisplayName("MTL 파일 조회 테스트")
	void mtl_파일_조회_성공() {
		// given
		when(clothesRepository.findByIdAndDeletedAtIsNull(TEST_CLOTHES_ID))
			.thenReturn(Optional.of(testClothes));
		when(s3Service.getObject(anyString())).thenReturn(TEST_MTL_CONTENT.getBytes());

		// when
		var response = clothesService.getClothesMtl(TEST_CLOTHES_ID);

		// then
		assertNotNull(response);
		assertEquals(TEST_CLOTHES_ID, response.clothesId());
		assertNotNull(response.file());
		assertTrue(response.fileName().endsWith(".mtl"));
	}

	@Test
	@DisplayName("ID로 옷 찾기 테스트")
	void id로_옷_찾기_성공() {
		// given
		when(clothesRepository.findByIdAndDeletedAtIsNull(TEST_CLOTHES_ID))
			.thenReturn(Optional.of(testClothes));

		// when
		Clothes found = clothesService.findById(TEST_CLOTHES_ID);

		// then
		assertNotNull(found);
		assertEquals(testClothes.getId(), found.getId());
		assertEquals(TEST_CLOTHES_NAME, found.getName());
	}

	@Test
	@DisplayName("ID로 옷 찾기 실패 - 존재하지 않는 상품")
	void id로_옷_찾기_실패() {
		// given
		when(clothesRepository.findByIdAndDeletedAtIsNull(INVALID_CLOTHES_ID))
			.thenReturn(Optional.empty());

		// when & then
		assertThrows(ClothesNotFoundException.class,
			() -> clothesService.findById(INVALID_CLOTHES_ID));
	}
}
