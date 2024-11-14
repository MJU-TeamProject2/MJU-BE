package com.example.demo.clothes.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.clothes.dto.request.CreateClothesRequest;
import com.example.demo.clothes.dto.request.UpdateClothesRequest;
import com.example.demo.clothes.dto.response.GetClothesDetailResponse;
import com.example.demo.clothes.dto.response.GetClothesFile;
import com.example.demo.clothes.dto.response.GetClothesResponse;
import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.entity.ClothesCategory;
import com.example.demo.clothes.entity.ClothesSize;
import com.example.demo.clothes.repository.ClothesRepository;
import com.example.demo.clothes.repository.ClothesSizeRepository;
import com.example.demo.common.dto.PageResponse;
import com.example.demo.common.exception.CustomException;
import com.example.demo.common.util.S3Service;
import com.example.demo.exception.ClothesErrorCode;
import com.example.demo.exception.ClothesNotFoundException;
import com.example.demo.exception.ClothesSizeNotFoundException;
import com.example.demo.util.PageUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClothesService {

	private final ClothesRepository clothesRepository;
	private final ClothesSizeRepository clothesSizeRepository;
	private final S3Service s3Service;

	public PageResponse<GetClothesResponse> getAllClothes(PageRequest pageRequest) {
		return PageUtils.toPageResponse(clothesRepository.findAllByDeletedAtIsNull(pageRequest))
			.map(clothes -> {
				String imageUrl = s3Service.generatePresignedUrl(clothes.getImageUrl());
				return GetClothesResponse.of(clothes, imageUrl);
			});
	}

	public GetClothesDetailResponse getClothesDetail(Long clothesId) {
		Clothes clothes = clothesRepository.findByIdAndDeletedAtIsNull(clothesId)
			.orElseThrow(ClothesNotFoundException::new);

		String imageUrl = s3Service.generatePresignedUrl(clothes.getImageUrl());
		String detailUrl = s3Service.generatePresignedUrl(clothes.getDetailUrl());
		String objectUrl = s3Service.generatePresignedUrl(clothes.getObjectKey());
		String objectFemaleUrl = s3Service.generatePresignedUrl(clothes.getObjectFemaleKey());
		String mtlUrl = s3Service.generatePresignedUrl(clothes.getMtlKey());
		return GetClothesDetailResponse.of(clothes, imageUrl, detailUrl, objectUrl, mtlUrl, objectFemaleUrl);
	}

	@Transactional
	public void createProduct(CreateClothesRequest createClothesRequest) {
		checkProductDuplicate(createClothesRequest.productNumber());

		String mainUrl = s3Service.uploadFile(createClothesRequest.mainImage(),
			createClothesRequest.category().toString());
		String detailUrl = s3Service.uploadFile(createClothesRequest.detailImage(),
			createClothesRequest.category().toString());
		String objectKey = s3Service.uploadFile(createClothesRequest.objectFile(), "object");
		String mtlKey = s3Service.uploadFile(createClothesRequest.mtlFile(), "mtl");

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
			.mtlKey(mtlKey)
			.build();

		ClothesSize clothesSize = ClothesSize.builder()
			.clothes(clothe)
			.size(createClothesRequest.size())
			.quantity(createClothesRequest.quantity())
			.build();

		clothe.getClothesSizeList().add(clothesSize);
		clothesRepository.save(clothe);
		clothesSizeRepository.save(clothesSize);
	}

	@Transactional
	public void updateProduct(Long clothesId, UpdateClothesRequest updateClothesRequest) {
		Clothes clothes = clothesRepository.findByIdAndDeletedAtIsNull(clothesId)
			.orElseThrow(ClothesNotFoundException::new);
		String mainUrl = null;
		String detailUrl = null;
		String objectKey = null;
		String objectFemaleKey = null;
		String mtlKey = null;
		String category = updateClothesRequest.category() == null ? clothes.getCategory().toString() :
			updateClothesRequest.category().toString();

		if (updateClothesRequest.mainImage() != null) {
			mainUrl = s3Service.uploadFile(updateClothesRequest.mainImage(),
				category);
		}
		if (updateClothesRequest.detailImage() != null) {
			detailUrl = s3Service.uploadFile(updateClothesRequest.detailImage(),
				category);
		}
		if (updateClothesRequest.objectFile() != null) {
			objectKey = s3Service.uploadFile(updateClothesRequest.objectFile(), "object");
		}
		if (updateClothesRequest.mtlFile() != null) {
			mtlKey = s3Service.uploadFile(updateClothesRequest.mtlFile(), "mtl");
		}
		if (updateClothesRequest.objectFemaleFile() != null) {
			objectFemaleKey = s3Service.uploadFile(updateClothesRequest.objectFemaleFile(), "object");
		}

		clothes.update(updateClothesRequest.category(),
			mainUrl,
			updateClothesRequest.name(),
			updateClothesRequest.price(),
			updateClothesRequest.genderCategory(),
			updateClothesRequest.productNumber(),
			updateClothesRequest.discount(),
			detailUrl,
			objectKey,
			mtlKey,
			objectFemaleKey);


		if (updateClothesRequest.size() == null)
			return;

		ClothesSize clothesSize = clothesSizeRepository.findByClothesAndSize(clothes, updateClothesRequest.size())
			.orElseThrow(ClothesSizeNotFoundException::new);
		clothesSize.update(updateClothesRequest.quantity());
	}

	@Transactional
	public void deleteProduct(Long clothesId) {
		Clothes clothes = clothesRepository.findByIdAndDeletedAtIsNull(clothesId)
			.orElseThrow(ClothesNotFoundException::new);
		List<String> keyList = new ArrayList<>();
		keyList.add(clothes.getImageUrl());
		keyList.add(clothes.getDetailUrl());
		keyList.add(clothes.getObjectKey());
		s3Service.fileDeletes(keyList);
		clothes.delete(LocalDateTime.now());
	}

	public GetClothesFile getClothesObject(Long clothesId) {
		Clothes clothes = clothesRepository.findByIdAndDeletedAtIsNull(clothesId)
			.orElseThrow(ClothesNotFoundException::new);
		String objectKey = clothes.getObjectKey();
		byte[] s3Object = s3Service.getObject(objectKey);
		String filename = objectKey.substring(objectKey.lastIndexOf("/") + 1);

		return GetClothesFile.of(clothesId, s3Object, filename);
	}

	public GetClothesFile getClothesMtl(Long clothesId) {
		Clothes clothes = clothesRepository.findByIdAndDeletedAtIsNull(clothesId)
			.orElseThrow(ClothesNotFoundException::new);
		String mtlKey = clothes.getMtlKey();
		byte[] s3Mtl = s3Service.getObject(mtlKey);
		String filename = mtlKey.substring(mtlKey.lastIndexOf("/") + 1);

		return GetClothesFile.of(clothesId, s3Mtl, filename);
	}

	private void checkProductDuplicate(String productNumber) {
		if (clothesRepository.findByProductNumberAndDeletedAtIsNull(productNumber).isPresent()) {
			throw new CustomException(ClothesErrorCode.CLOTHES_DUPLICATE);
		}

	}

	public PageResponse<GetClothesResponse> getClothesByCategory(PageRequest pageRequest,
		ClothesCategory clothesCategory) {
		return PageUtils.toPageResponse(
				clothesRepository.findByCategoryAndDeletedAtIsNull(pageRequest, clothesCategory))
			.map(clothes -> {
				String imageUrl = s3Service.generatePresignedUrl(clothes.getImageUrl());
				return GetClothesResponse.of(clothes, imageUrl);
			});
	}

	public Clothes findById(Long clothesId) {
		return clothesRepository.findByIdAndDeletedAtIsNull(clothesId).orElseThrow(ClothesNotFoundException::new);
	}

}
