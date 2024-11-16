package com.example.demo.admin.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.admin.dto.response.AdminLoginResponse;
import com.example.demo.admin.entity.Admin;
import com.example.demo.admin.repository.AdminRepository;
import com.example.demo.common.security.TokenProvider;
import com.example.demo.common.util.auth.Auth;
import com.example.demo.common.util.auth.AuthService;
import com.example.demo.util.TestResultLogger;

import lombok.extern.slf4j.Slf4j;

@ExtendWith({MockitoExtension.class, TestResultLogger.class})
@Slf4j
class AdminServiceTest {

	@InjectMocks
	private AdminService adminService;

	@Mock
	AdminRepository adminRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private TokenProvider tokenProvider;
	@Mock
	private AuthService authService;
	private Admin admin;

	// 테스트용 Auth 객체 생성 헬퍼 메소드
	private Auth createTestAuth(String email, String refreshToken) {
		Auth auth = Auth.createAuth(email);
		if (refreshToken != null) {
			auth.updateRefreshToken(refreshToken);
		}
		return auth;
	}

	@BeforeEach
	void setUp() {
		admin = Admin.builder()
			.code("Admin001")
			.password(passwordEncoder.encode("admin01"))
			.build();
	}

	@Test
	@DisplayName("관리자 로그인 성공 테스트")
	void 관리자_로그인_성공() {
		// Given
		String code = "Admin001";
		String password = "admin01";
		String accessToken = "accessToken";
		String refreshToken = "refreshToken";
		Auth auth = createTestAuth(code, null);

		when(adminRepository.findByCode(code)).thenReturn(Optional.of(admin));
		when(passwordEncoder.matches(password, admin.getPassword())).thenReturn(true);
		when(tokenProvider.createAccessToken(admin.getId(), admin.getRole()))
			.thenReturn(accessToken);
		when(tokenProvider.createRefreshToken(admin.getId(), admin.getRole()))
			.thenReturn(refreshToken);
		when(authService.findByCode(code)).thenReturn(auth);

		// When
		AdminLoginResponse response = adminService.login(code, password);

		// Then
		assertNotNull(response);
		assertEquals(accessToken, response.accessToken());
		assertEquals(refreshToken, response.refreshToken());
		verify(authService).findByCode(code);
	}
}
