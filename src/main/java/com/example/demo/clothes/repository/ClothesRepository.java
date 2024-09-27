package com.example.demo.clothes.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.clothes.entity.Clothes;

import jakarta.validation.constraints.NotNull;

public interface ClothesRepository extends JpaRepository<Clothes, Long> {
	Page<Clothes> findAll(@NotNull Pageable pageable);

	Optional<Clothes> findById(@NotNull Long clothesId);
}
