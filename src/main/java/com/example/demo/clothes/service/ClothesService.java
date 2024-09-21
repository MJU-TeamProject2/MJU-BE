package com.example.demo.clothes.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.clothes.dto.GetClothesResponse;
import com.example.demo.clothes.repository.ClothesRepository;
import com.example.demo.common.dto.PageResponse;
import com.example.demo.util.PageUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClothesService {
	private final ClothesRepository clothesRepository;

	public PageResponse<GetClothesResponse> getAllClothes(PageRequest pageRequest) {
		return PageUtils.toPageResponse(clothesRepository.findAll(pageRequest)).map(GetClothesResponse::from);
	}
}
