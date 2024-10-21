package com.example.demo.clothes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.entity.ClothesSize;
import com.example.demo.clothes.entity.Size;

public interface ClothesSizeRepository extends JpaRepository<ClothesSize, Long> {
	Optional<ClothesSize> findByClothesAndSize(Clothes clothes, Size size);
}
