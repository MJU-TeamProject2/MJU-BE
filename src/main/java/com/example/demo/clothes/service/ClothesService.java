package com.example.demo.clothes.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.clothes.dto.response.GetClothesDetailResponse;
import com.example.demo.clothes.dto.response.GetClothesResponse;
import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.repository.ClothesRepository;
import com.example.demo.common.dto.PageResponse;
import com.example.demo.common.util.S3Service;
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
		// byte[] s3Object = s3Service.getObject(objectKey);

		String url = s3Service.generatePresignedUrl(objectKey);
		return GetClothesDetailResponse.from(clothes, url);
	}

}
