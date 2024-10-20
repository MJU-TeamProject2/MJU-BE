package com.example.demo.clothes.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.entity.ClothesCategory;

public interface ClothesRepository extends JpaRepository<Clothes, Long> {
	Page<Clothes> findAllByDeletedAtIsNull(Pageable pageable);

	Optional<Clothes> findByIdAndDeletedAtIsNull(Long clothesId);

	Optional<Clothes> findByProductNumberAndDeletedAtIsNull(String productNumber);

	Page<Clothes> findByCategoryAndDeletedAtIsNull(PageRequest pageRequest, ClothesCategory category);
}
