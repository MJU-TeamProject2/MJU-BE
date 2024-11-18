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

	private static final String ADMIN_CODE = "Admin001";
	private static final String ADMIN_PASSWORD = "admin01";
	private static final String ACCESS_TOKEN = "accessToken";
	private static final String REFRESH_TOKEN = "refreshToken";

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
		Auth auth = createTestAuth(ADMIN_CODE, null);

		when(adminRepository.findByCode(ADMIN_CODE)).thenReturn(Optional.of(admin));
		when(passwordEncoder.matches(ADMIN_PASSWORD, admin.getPassword())).thenReturn(true);
		when(tokenProvider.createAccessToken(admin.getId(), admin.getRole()))
			.thenReturn(ACCESS_TOKEN);
		when(tokenProvider.createRefreshToken(admin.getId(), admin.getRole()))
			.thenReturn(REFRESH_TOKEN);
		when(authService.findByCode(ADMIN_CODE)).thenReturn(auth);

		// When
		AdminLoginResponse response = adminService.login(ADMIN_CODE, ADMIN_PASSWORD);

		// Then
		assertNotNull(response);
		assertEquals(ACCESS_TOKEN, response.accessToken());
		assertEquals(REFRESH_TOKEN, response.refreshToken());
		verify(authService).findByCode(ADMIN_CODE);
	}
}
