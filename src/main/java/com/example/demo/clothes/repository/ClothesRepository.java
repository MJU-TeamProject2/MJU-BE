package com.example.demo.clothes.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.entity.ClothesCategory;

public interface ClothesRepository extends JpaRepository<Clothes, Long> {
	Page<Clothes> findAll(Pageable pageable);

	Optional<Clothes> findById(Long clothesId);

	Optional<Clothes> findByProductNumber(String productNumber);

	Page<Clothes> findByCategory(PageRequest pageRequest, ClothesCategory category);
}
