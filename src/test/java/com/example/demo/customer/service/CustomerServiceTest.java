package com.example.demo.customer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.security.TokenProvider;
import com.example.demo.common.util.S3Service;
import com.example.demo.common.util.auth.Auth;
import com.example.demo.common.util.auth.AuthService;
import com.example.demo.customer.dto.response.GetCustomerResponse;
import com.example.demo.customer.dto.response.LoginResponse;
import com.example.demo.customer.entity.BodyType;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.entity.Gender;
import com.example.demo.customer.repository.CustomerRepository;
import com.example.demo.util.TestResultLogger;

import lombok.extern.slf4j.Slf4j;

@ExtendWith({MockitoExtension.class, TestResultLogger.class})
@Slf4j
class CustomerServiceTest {

	private static final String EMAIL = "test@test.com";
	private static final String NOT_REGISTED_EMAIL = "nonexistent@test.com";
	private static final String PASSWORD = "password";
	private static final String NOT_REGISTED_PASSWORD = "wrongpassword";
	private static final String NAME = "Customer";
	private static final String NICK_NAME = "Customer";
	private static final int AGE = 25;
	private static final Gender GENDER = Gender.M;
	private static final String PHONE_NUMBER = "010-1234-5678";
	private static final String ACCESS_TOKEN = "accessToken";
	private static final String REFRESH_TOKEN = "refreshToken";
	private static final BodyType BODY_TYPE = BodyType.RECTANGLE;
	private static final String EXPECTED_BODY_OBJECT_URL = "https://s3-bucket.com/object/MALE_RECTANGLE.obj";
	private static final String URL_KEY = "object/";
	private static final String EXTENSION = ".obj";
	private static final Long CUSTOMER_ID = 1L;

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
	@Mock
	private S3Service s3Service;
	private Customer customer;

	// 테스트용 Auth 객체 생성 헬퍼 메소드
	private Auth createTestAuth(String refreshToken) {
		Auth auth = Auth.createAuth(CustomerServiceTest.EMAIL);
		if (refreshToken != null) {
			auth.updateRefreshToken(refreshToken);
		}
		return auth;
	}

	@BeforeEach
	void setUp() {
		customer = Customer.builder()
			.email(EMAIL)
			.password(passwordEncoder.encode(PASSWORD))
			.name(NAME)
			.nickName(NICK_NAME)
			.age(AGE)
			.gender(GENDER)
			.phoneNumber(PHONE_NUMBER)
			.bodyType(BODY_TYPE)
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
	@DisplayName("이메일 중복 체크 테스트 - 중복되지 않은 경우")
	void 이메일_중복체크_중복X() {
		// Given
		when(customerRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

		// When & Then
		assertDoesNotThrow(() -> customerService.checkEmailDuplicate(EMAIL));
	}

	@Test
	@DisplayName("이메일 중복 체크 테스트 - 중복 되는 경우")
	void 이메일_중복체크_중복O() {
		// Given
		when(customerRepository.findByEmail(EMAIL)).thenReturn(Optional.of(customer));

		// When & Then
		assertThrows(CustomException.class, () -> customerService.checkEmailDuplicate(EMAIL));
	}

	@Test
	@DisplayName("로그인 성공 테스트")
	void 로그인_성공() {
		// Given
		Auth auth = createTestAuth(null);

		when(customerRepository.findByEmail(EMAIL)).thenReturn(Optional.of(customer));
		when(passwordEncoder.matches(PASSWORD, customer.getPassword())).thenReturn(true);
		when(tokenProvider.createAccessToken(customer.getId(), customer.getRole()))
			.thenReturn(ACCESS_TOKEN);
		when(tokenProvider.createRefreshToken(customer.getId(), customer.getRole()))
			.thenReturn(REFRESH_TOKEN);
		when(authService.findByCode(EMAIL)).thenReturn(auth);
		when(s3Service.generatePresignedUrl(URL_KEY + customer.getGender() + customer.getBodyType() + EXTENSION))
			.thenReturn(EXPECTED_BODY_OBJECT_URL);

		// When
		LoginResponse response = customerService.login(EMAIL, PASSWORD);

		// Then
		assertNotNull(response);
		assertEquals(ACCESS_TOKEN, response.accessToken());
		assertEquals(REFRESH_TOKEN, response.refreshToken());
		assertEquals(EXPECTED_BODY_OBJECT_URL, response.bodyObjectUrl());
		verify(authService).findByCode(EMAIL);
		verify(s3Service).generatePresignedUrl(URL_KEY + customer.getGender() + customer.getBodyType() + EXTENSION);
	}

	@Test
	@DisplayName("로그인 실패 테스트 - 사용자를 찾을 수 없음")
	void 로그인_실패_사용자_찾지_못함() {
		// Given
		when(customerRepository.findByEmail(NOT_REGISTED_EMAIL)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(CustomException.class,
			() -> customerService.login(NOT_REGISTED_EMAIL, PASSWORD));
	}

	@Test
	@DisplayName("로그인 실패 테스트 - 잘못된 비밀번호")
	void 로그인_실패_비밀번호_틀림() {
		// Given
		when(customerRepository.findByEmail(EMAIL)).thenReturn(Optional.of(customer));
		when(passwordEncoder.matches(NOT_REGISTED_PASSWORD, customer.getPassword())).thenReturn(false);

		// When & Then
		assertThrows(CustomException.class,
			() -> customerService.login(EMAIL, NOT_REGISTED_PASSWORD));
	}

	@Test
	@DisplayName("프로필 조회 성공 테스트")
	void 프로필_조회_성공() {
		// Given
		when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
		when(s3Service.generatePresignedUrl(URL_KEY + customer.getGender() + customer.getBodyType() + EXTENSION))
			.thenReturn(EXPECTED_BODY_OBJECT_URL);

		// When
		GetCustomerResponse response = customerService.retrieveProfile(CUSTOMER_ID);

		// Then
		assertNotNull(response);
		assertEquals(EMAIL, response.email());
		assertEquals(NAME, response.name());
		assertEquals(NICK_NAME, response.nickName());
		assertEquals(AGE, response.age());
		assertEquals(PHONE_NUMBER, response.phoneNumber());
		assertEquals(EXPECTED_BODY_OBJECT_URL, response.bodyObjUrl());
		verify(customerRepository).findById(CUSTOMER_ID);
		verify(s3Service).generatePresignedUrl(URL_KEY + customer.getGender() + customer.getBodyType() + EXTENSION);
	}

	@Test
	@DisplayName("프로필 조회 실패 테스트 - 존재하지 않는 사용자")
	void 프로필_조회_실패() {
		// Given
		when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(CustomException.class, () -> customerService.retrieveProfile(CUSTOMER_ID));
		verify(customerRepository).findById(CUSTOMER_ID);
	}
}
