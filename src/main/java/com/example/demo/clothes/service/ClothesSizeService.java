package com.example.demo.clothes.service;

import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.entity.ClothesSize;
import com.example.demo.clothes.entity.Size;
import com.example.demo.clothes.repository.ClothesSizeRepository;
import com.example.demo.exception.ClothesInsufficientStockException;
import com.example.demo.exception.ClothesSizeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClothesSizeService {
  private final ClothesSizeRepository clothesSizeRepository;

  public ClothesSize findByClothesIdAndSize(Clothes clothes, Size size) {
    return clothesSizeRepository.findByClothesAndSize(clothes, size).orElseThrow(ClothesSizeNotFoundException::new);
  }

  public void updateClothesQuantity(ClothesSize clothesSize, Integer quantity) {
    if (quantity < 0) throw new ClothesInsufficientStockException();
    clothesSize.update(quantity);
    clothesSizeRepository.save(clothesSize);
  }
}
