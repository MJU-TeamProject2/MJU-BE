package com.example.demo.admin.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.admin.entity.Admin;
import com.example.demo.admin.repository.AdminRepository;
import com.example.demo.common.util.auth.Auth;
import com.example.demo.common.util.auth.AuthRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

	private final AdminRepository adminRepository;
	private final AuthRepository authRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${admin.code}")
	private String adminCode;

	@Value("${admin.password}")
	private String adminPassword;

	@Override
	public void run(String... args) {
		createAdminIfNotExists();
	}

	private void createAdminIfNotExists() {
		// 이미 관리자 계정이 존재하는지 확인
		if (!adminRepository.existsByCode(adminCode)) {
			// Admin 엔티티 생성
			Admin admin = Admin.builder()
				.code(adminCode)
				.password(passwordEncoder.encode(adminPassword))
				.name("관리자")
				.build();

			// Admin 저장
			adminRepository.save(admin);

			// Auth 엔티티 생성
			Auth auth = new Auth(null, adminCode);

			// Auth 저장
			authRepository.save(auth);
		}
	}
}
