package com.example.demo.clothes.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.clothes.entity.Clothes;

public interface ClothesRepository extends JpaRepository<Clothes, Long> {
	Page<Clothes> findAll(Pageable pageable);
}
