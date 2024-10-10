package com.example.demo.common.util.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, Integer> {
	Optional<Auth> findByCode(String code);

	Optional<Auth> findByRefreshToken(String refreshToken);
}
