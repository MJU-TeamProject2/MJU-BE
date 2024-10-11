package com.example.demo.admin.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.admin.dto.response.AdminLoginResponse;
import com.example.demo.admin.entity.Admin;
import com.example.demo.admin.repository.AdminRepository;
import com.example.demo.common.exception.CustomException;
import com.example.demo.common.security.TokenProvider;
import com.example.demo.common.util.auth.Auth;
import com.example.demo.common.util.auth.AuthService;
import com.example.demo.exception.AdminErrorCode;
import com.example.demo.exception.AdminNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final PasswordEncoder passwordEncoder;
	private final TokenProvider tokenProvider;
	private final AuthService authService;
	private final AdminRepository adminRepository;

	@Transactional
	public AdminLoginResponse login(String code, String password) {
		Admin admin = adminRepository.findByCode(code)
			.orElseThrow(AdminNotFoundException::new);
		if (!passwordEncoder.matches(password, admin.getPassword())) {
			throw new CustomException(AdminErrorCode.ADMIN_WRONG_PASSWORD);
		}

		String accessToken = tokenProvider.createAccessToken(admin.getId(), admin.getRole());
		String refreshToken = tokenProvider.createRefreshToken(admin.getId(), admin.getRole());

		Auth adminAuth = authService.findByCode(admin.getCode());
		adminAuth.updateRefreshToken(refreshToken);

		return AdminLoginResponse.builder()
			.name(admin.getName())
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.role(admin.getRole())
			.build();
	}
}
