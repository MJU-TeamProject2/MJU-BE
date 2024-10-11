package com.example.demo.admin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.admin.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	Optional<Admin> findByCode(String code);

	boolean existsByCode(String adminCode);
}
