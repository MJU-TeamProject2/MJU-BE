package com.example.demo.customer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.customer.entity.CustomerAuth;

public interface CustomerAuthRepository extends JpaRepository<CustomerAuth, Integer> {
	Optional<CustomerAuth> findByCustomerId(Long customerId);

	Optional<CustomerAuth> findByRefreshToken(String refreshToken);
}
