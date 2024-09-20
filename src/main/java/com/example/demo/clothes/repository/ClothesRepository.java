package com.example.demo.clothes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.clothes.entity.Clothes;

public interface ClothesRepository extends JpaRepository<Clothes, Long> {
}
