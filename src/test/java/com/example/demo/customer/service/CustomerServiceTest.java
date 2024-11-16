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
import com.example.demo.common.util.auth.AuthService;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.entity.Gender;
import com.example.demo.customer.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

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
	@DisplayName("이메일 중복 체크 - 중복되지 않은 경우")
	void 이메일_중복체크_중복X() {
		// Given
		String email = "test@test.com";
		when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());

		// When & Then
		assertDoesNotThrow(() -> customerService.checkEmailDuplicate(email));
	}

	@Test
	@DisplayName("이메일 중복 체크 - 중복 되는 경우")
	void 이메일_중복체크_중복O() {
		// Given
		String email = "test@test.com";
		when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));

		// When & Then
		assertThrows(CustomException.class, () -> customerService.checkEmailDuplicate(email));
	}

	// @Test
	// void login() {
	// }
}
