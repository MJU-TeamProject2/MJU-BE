package com.example.demo.clothes.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.clothes.dto.request.CreateClothesRequest;
import com.example.demo.clothes.dto.response.GetClothesDetailResponse;
import com.example.demo.clothes.dto.response.GetClothesResponse;
import com.example.demo.clothes.dto.response.GetClothesObject;
import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.entity.ClothesSize;
import com.example.demo.clothes.repository.ClothesRepository;
import com.example.demo.common.dto.PageResponse;
import com.example.demo.common.exception.CustomException;
import com.example.demo.common.util.S3Service;
import com.example.demo.exception.ClothesErrorCode;
import com.example.demo.exception.ClothesNotFoundException;
import com.example.demo.util.PageUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClothesService {

	private final ClothesRepository clothesRepository;
	private final S3Service s3Service;

	public PageResponse<GetClothesResponse> getAllClothes(PageRequest pageRequest) {
		return PageUtils.toPageResponse(clothesRepository.findAll(pageRequest)).map(GetClothesResponse::from);
	}

	public GetClothesDetailResponse getClothesDetail(Long clothesId) {
		Clothes clothes = clothesRepository.findById(clothesId).orElseThrow(ClothesNotFoundException::new);
		String objectKey = clothes.getObjectKey();

		String url = s3Service.generatePresignedUrl(objectKey);
		return GetClothesDetailResponse.from(clothes, url);
	}

	public void createProduct(CreateClothesRequest createClothesRequest) {
		checkProductDuplicate(createClothesRequest.productNumber());

		String mainUrl = s3Service.uploadFile(createClothesRequest.mainImage(),
			createClothesRequest.category().toString());
		String detailUrl = s3Service.uploadFile(createClothesRequest.detailImage(),
			createClothesRequest.category().toString());
		String objectKey = s3Service.uploadFile(createClothesRequest.objectFile(), "object");

		Clothes clothe = Clothes.builder()
			.category(createClothesRequest.category())
			.imageUrl(mainUrl)
			.name(createClothesRequest.name())
			.price(createClothesRequest.price())
			.genderCategory(createClothesRequest.genderCategory())
			.productNumber(createClothesRequest.productNumber())
			.discount(createClothesRequest.discount())
			.detailUrl(detailUrl)
			.objectKey(objectKey)
			.build();

		ClothesSize clothesSize = ClothesSize.builder()
			.clothes(clothe)
			.size(createClothesRequest.size())
			.quantity(createClothesRequest.quantity())
			.build();

		clothe.getClothesSizeList().add(clothesSize);
		clothesRepository.save(clothe);
	}

	public GetClothesObject getClothesObject(Long clothesId) {
		Clothes clothes = clothesRepository.findById(clothesId).orElseThrow(ClothesNotFoundException::new);
		String objectKey = clothes.getObjectKey();
		byte[] s3Object = s3Service.getObject(objectKey);
		String filename = objectKey.substring(objectKey.lastIndexOf("/") + 1);

		return GetClothesObject.of(clothesId, s3Object, filename);

	}

	private void checkProductDuplicate(String productNumber) {
		if (clothesRepository.findByProductNumber(productNumber).isPresent()) {
			throw new CustomException(ClothesErrorCode.CLOTHES_DUPLICATE);
		}

	}
}
