package com.example.demo.customer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.security.TokenProvider;
import com.example.demo.common.util.auth.Auth;
import com.example.demo.common.util.auth.AuthService;
import com.example.demo.customer.dto.response.LoginResponse;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.entity.Gender;
import com.example.demo.customer.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class CustomerServiceTest {

	@ExtendWith(TestResultLogger.class)
	static class TestResultLogger implements TestWatcher {
		private static final Logger log = LoggerFactory.getLogger(TestResultLogger.class);

		@Override
		public void testSuccessful(ExtensionContext context) {
			log.info("✅ {} 성공!", context.getDisplayName());
		}

		@Override
		public void testFailed(ExtensionContext context, Throwable cause) {
			log.error("❌ {} 실패! 원인: {}", context.getDisplayName(), cause.getMessage());
		}
	}

	@InjectMocks
	private CustomerService customerService;

	@Mock
	private CustomerRepository customerRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private TokenProvider tokenProvider;
	@Mock
	private AuthService authService;

	private Customer customer;

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
		customer = Customer.builder()
				.email("test@test.com")
				.password(passwordEncoder.encode("qwer"))
				.name("Customer1")
				.nickName("Customer1")
				.age(25)
				.gender(Gender.M)
				.phoneNumber("000-1234-5678")
				.build();
	}

	@Test
	@DisplayName("고객 회원가입 성공 테스트")
	void 회원가입_성공() {
		// given
		when(customerRepository.save(any(Customer.class))).thenReturn(customer);

		// when
		customerService.register(customer);

		// then
		verify(customerRepository).save(customer);
		verify(authService).createAuth(customer.getEmail());
	}

	@Test
	@DisplayName("이메일 중복 체크 - 중복되지 않은 경우 테스트 ")
	void 이메일_중복체크_중복X() {
		// Given
		String email = "test@test.com";
		when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());

		// When & Then
		assertDoesNotThrow(() -> customerService.checkEmailDuplicate(email));
	}

	@Test
	@DisplayName("이메일 중복 체크 - 중복 되는 경우 테스트")
	void 이메일_중복체크_중복O() {
		// Given
		String email = "test@test.com";
		when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));

		// When & Then
		assertThrows(CustomException.class, () -> customerService.checkEmailDuplicate(email));
	}

	@Test
	@DisplayName("로그인 성공 테스트")
	void 로그인_성공() {
		// Given
		String email = "test@test.com";
		String password = "qwer";
		String accessToken = "accessToken";
		String refreshToken = "refreshToken";
		Auth auth = createTestAuth(email, null);

		when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));
		when(passwordEncoder.matches(password, customer.getPassword())).thenReturn(true);
		when(tokenProvider.createAccessToken(customer.getId(), customer.getRole()))
			.thenReturn(accessToken);
		when(tokenProvider.createRefreshToken(customer.getId(), customer.getRole()))
			.thenReturn(refreshToken);
		when(authService.findByCode(email)).thenReturn(auth);

		// When
		LoginResponse response = customerService.login(email, password);

		// Then
		assertNotNull(response);
		assertEquals(accessToken, response.accessToken());
		assertEquals(refreshToken, response.refreshToken());
		verify(authService).findByCode(email);
	}
}
