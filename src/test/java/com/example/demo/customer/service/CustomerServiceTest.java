package com.example.demo.customer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.demo.customer.dto.request.AddAddressRequest;
import com.example.demo.customer.dto.request.AddPaymentRequest;
import com.example.demo.customer.dto.request.UpdateAddressRequest;
import com.example.demo.customer.dto.request.UpdatePaymentRequest;
import com.example.demo.customer.dto.response.GetAddressDetailResponse;
import com.example.demo.customer.dto.response.GetAddressResponse;
import com.example.demo.customer.dto.response.GetPaymentDetailResponse;
import com.example.demo.customer.dto.response.GetPaymentResponse;
import com.example.demo.customer.entity.Address;
import com.example.demo.customer.entity.CardProvider;
import com.example.demo.customer.entity.Payment;
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
import com.example.demo.customer.dto.request.ProfileUpdateRequest;
import com.example.demo.customer.dto.response.GetCustomerResponse;
import com.example.demo.customer.dto.response.LoginResponse;
import com.example.demo.customer.entity.BodyType;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.entity.Gender;
import com.example.demo.customer.repository.CustomerRepository;
import com.example.demo.util.TestResultLogger;

import java.util.List;

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
	private static final int UPDATED_AGE = 30;
	private static final String UPDATED_EMAIL = "updated@test.com";
	private static final String UPDATED_NAME = "Updated Customer";
	private static final String UPDATED_NICK_NAME = "Updated Nick";
	private static final String UPDATED_PHONE_NUMBER = "010-9876-5432";
	private static final int UPDATED_WEIGHT = 72;
	private static final int UPDATED_HEIGHT = 176;

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
	@Mock
	private AddressService addressService;
	@Mock
	private PaymentService paymentService;

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

	@Test
	@DisplayName("프로필 업데이트 성공 테스트")
	void 프로필_업데이트_성공() {
		// Given
		ProfileUpdateRequest request = new ProfileUpdateRequest(
			UPDATED_NAME,
			UPDATED_NICK_NAME,
			UPDATED_AGE,
			UPDATED_EMAIL,
			UPDATED_PHONE_NUMBER,
			UPDATED_HEIGHT,
			UPDATED_WEIGHT,
			BODY_TYPE
		);

		when(customerRepository.getReferenceById(CUSTOMER_ID)).thenReturn(customer);
		doNothing().when(authService).update(EMAIL, UPDATED_EMAIL);

		// When
		customerService.updateProfile(CUSTOMER_ID, request);

		// Then
		assertEquals(UPDATED_EMAIL, customer.getEmail());
		assertEquals(UPDATED_NAME, customer.getName());
		assertEquals(UPDATED_NICK_NAME, customer.getNickName());
		assertEquals(UPDATED_AGE, customer.getAge());
		assertEquals(UPDATED_PHONE_NUMBER, customer.getPhoneNumber());
		assertEquals(UPDATED_WEIGHT, customer.getWeight());
		assertEquals(UPDATED_HEIGHT, customer.getHeight());
		verify(customerRepository).getReferenceById(CUSTOMER_ID);
		verify(authService).update(EMAIL, UPDATED_EMAIL);
	}

	@Test
	@DisplayName("ID로 고객 조회 성공 테스트")
	void id로_고객_조회_성공() {
		// Given
		when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));

		// When
		Customer foundCustomer = customerService.findById(CUSTOMER_ID);

		// Then
		assertNotNull(foundCustomer);
		assertEquals(EMAIL, foundCustomer.getEmail());
		assertEquals(NAME, foundCustomer.getName());
		assertEquals(NICK_NAME, foundCustomer.getNickName());
		assertEquals(AGE, foundCustomer.getAge());
		assertEquals(PHONE_NUMBER, foundCustomer.getPhoneNumber());
		verify(customerRepository).findById(CUSTOMER_ID);
	}

	@Test
	@DisplayName("ID로 고객 조회 실패 테스트 - 존재하지 않는 사용자")
	void id로_고객_조회_실패() {
		// Given
		when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(CustomException.class, () -> customerService.findById(CUSTOMER_ID));
		verify(customerRepository).findById(CUSTOMER_ID);
	}

	@Test
	@DisplayName("고객의 주소 목록을 조회한다")
	void 주소_목록_조회_성공() {
		// Given
		List<Address> addresses = List.of(
				Address.builder()
						.id(1L)
						.name("집")
						.recipient("홍길동")
						.build()
		);
		when(customerRepository.getReferenceById(CUSTOMER_ID)).thenReturn(customer);
		when(addressService.findByCustomerAndDeletedAtIsNull(customer)).thenReturn(addresses);

		// When
		List<GetAddressResponse> responses = customerService.getAddresses(CUSTOMER_ID);

		// Then
		assertNotNull(responses);
		assertEquals(1, responses.size());
		assertEquals("집", responses.get(0).name());
		assertEquals("홍길동", responses.get(0).recipient());
		verify(customerRepository).getReferenceById(CUSTOMER_ID);
		verify(addressService).findByCustomerAndDeletedAtIsNull(customer);
	}

	@Test
	@DisplayName("고객의 주소 상세 정보를 조회한다")
	void 주소_상세_조회_성공() {
		// Given
		Long addressId = 1L;
		Address address = Address.builder()
				.id(addressId)
				.name("집")
				.recipient("홍길동")
				.zipCode("12345")
				.baseAddress("서울시 강남구")
				.detailAddress("101동 101호")
				.build();

		when(customerRepository.getReferenceById(CUSTOMER_ID)).thenReturn(customer);
		when(addressService.findByIdAndCustomerAndDeletedAtIsNull(addressId, customer)).thenReturn(address);

		// When
		GetAddressDetailResponse response = customerService.getAddressDetail(CUSTOMER_ID, addressId);

		// Then
		assertNotNull(response);
		assertEquals("집", response.name());
		assertEquals("12345", response.zipCode());
		verify(customerRepository).getReferenceById(CUSTOMER_ID);
		verify(addressService).findByIdAndCustomerAndDeletedAtIsNull(addressId, customer);
	}

	@Test
	@DisplayName("새로운 주소를 추가한다")
	void 주소_추가_성공() {
		// Given
		AddAddressRequest request = new AddAddressRequest(
				"집",
				"홍길동",
				"12345",
				"서울시 강남구",
				"101동 101호"
		);

		when(customerRepository.getReferenceById(CUSTOMER_ID)).thenReturn(customer);
		doNothing().when(addressService).addAddress(customer, request);

		// When
		customerService.addAddress(CUSTOMER_ID, request);

		// Then
		verify(customerRepository).getReferenceById(CUSTOMER_ID);
		verify(addressService).addAddress(customer, request);
	}

	@Test
	@DisplayName("주소를 수정한다")
	void 주소_수정_성공() {
		// Given
		Long addressId = 1L;
		Address address = Address.builder()
				.id(addressId)
				.build();
		UpdateAddressRequest request = new UpdateAddressRequest(
				addressId,
				"회사",
				"홍길동",
				"54321",
				"서울시 서초구",
				"202동 202호"
		);

		when(customerRepository.getReferenceById(CUSTOMER_ID)).thenReturn(customer);
		when(addressService.findByIdAndCustomerAndDeletedAtIsNull(addressId, customer)).thenReturn(address);
		doNothing().when(addressService).updateAddress(address, request);

		// When
		customerService.updateAddress(CUSTOMER_ID, request);

		// Then
		verify(customerRepository).getReferenceById(CUSTOMER_ID);
		verify(addressService).findByIdAndCustomerAndDeletedAtIsNull(addressId, customer);
		verify(addressService).updateAddress(address, request);
	}

	@Test
	@DisplayName("주소를 삭제한다")
	void 주소_삭제_성공() {
		// Given
		Long addressId = 1L;
		Address address = Address.builder()
				.id(addressId)
				.build();

		when(customerRepository.getReferenceById(CUSTOMER_ID)).thenReturn(customer);
		when(addressService.findByIdAndCustomerAndDeletedAtIsNull(addressId, customer)).thenReturn(address);
		doNothing().when(addressService).deleteAddress(address);

		// When
		customerService.deleteAddress(CUSTOMER_ID, addressId);

		// Then
		verify(customerRepository).getReferenceById(CUSTOMER_ID);
		verify(addressService).findByIdAndCustomerAndDeletedAtIsNull(addressId, customer);
		verify(addressService).deleteAddress(address);
	}

	@Test
	@DisplayName("고객의 결제 수단 목록을 조회한다")
	void 결제수단_목록_조회_성공() {
		// Given
		List<Payment> payments = List.of(
				Payment.builder()
						.id(1L)
						.cardNumber("1234-5678-9012-3456")
						.cardProvider(CardProvider.LOTTE)
						.build()
		);
		when(customerRepository.getReferenceById(CUSTOMER_ID)).thenReturn(customer);
		when(paymentService.findByCustomerAndDeletedAtIsNull(customer)).thenReturn(payments);

		// When
		List<GetPaymentResponse> responses = customerService.getPayments(CUSTOMER_ID);

		// Then
		assertNotNull(responses);
		assertEquals(1, responses.size());
		assertEquals("1234-5678-9012-3456", responses.get(0).cardNumber());
		assertEquals(CardProvider.LOTTE, responses.get(0).cardProvider());
		verify(customerRepository).getReferenceById(CUSTOMER_ID);
		verify(paymentService).findByCustomerAndDeletedAtIsNull(customer);
	}

	@Test
	@DisplayName("결제 수단 상세 정보를 조회한다")
	void 결제수단_상세_조회_성공() {
		// Given
		Long paymentId = 1L;
		Payment payment = Payment.builder()
				.id(paymentId)
				.cardNumber("1234-5678-9012-3456")
				.cardProvider(CardProvider.LOTTE)
				.expiryDate("12/25")
				.build();

		when(customerRepository.getReferenceById(CUSTOMER_ID)).thenReturn(customer);
		when(paymentService.findByIdAndCustomerAndDeletedAtIsNull(paymentId, customer)).thenReturn(payment);

		// When
		GetPaymentDetailResponse response = customerService.getPaymentDetail(CUSTOMER_ID, paymentId);

		// Then
		assertNotNull(response);
		assertEquals("1234-5678-9012-3456", response.cardNumber());
		assertEquals(CardProvider.LOTTE, response.cardProvider());
		assertEquals("12/25", response.expiryDate());
		verify(customerRepository).getReferenceById(CUSTOMER_ID);
		verify(paymentService).findByIdAndCustomerAndDeletedAtIsNull(paymentId, customer);
	}

	@Test
	@DisplayName("새로운 결제 수단을 추가한다")
	void 결제수단_추가_성공() {
		// Given
		AddPaymentRequest request = new AddPaymentRequest(
				"1234-5678-9012-3456",
				CardProvider.LOTTE,
				"12/25"
		);

		when(customerRepository.getReferenceById(CUSTOMER_ID)).thenReturn(customer);
		doNothing().when(paymentService).addPayment(customer, request);

		// When
		customerService.addPayment(CUSTOMER_ID, request);

		// Then
		verify(customerRepository).getReferenceById(CUSTOMER_ID);
		verify(paymentService).addPayment(customer, request);
	}

	@Test
	@DisplayName("결제 수단을 수정한다")
	void 결제수단_수정_성공() {
		// Given
		Long paymentId = 1L;
		Payment payment = Payment.builder()
				.id(paymentId)
				.build();
		UpdatePaymentRequest request = new UpdatePaymentRequest(
				paymentId,
				"9876-5432-1098-7654",
				CardProvider.LOTTE,
				"06/28"
		);

		when(customerRepository.getReferenceById(CUSTOMER_ID)).thenReturn(customer);
		when(paymentService.findByIdAndCustomerAndDeletedAtIsNull(paymentId, customer)).thenReturn(payment);
		doNothing().when(paymentService).updatePayment(payment, request);

		// When
		customerService.updatePayment(CUSTOMER_ID, request);

		// Then
		verify(customerRepository).getReferenceById(CUSTOMER_ID);
		verify(paymentService).findByIdAndCustomerAndDeletedAtIsNull(paymentId, customer);
		verify(paymentService).updatePayment(payment, request);
	}

	@Test
	@DisplayName("결제 수단을 삭제한다")
	void 결제수단_삭제_성공() {
		// Given
		Long paymentId = 1L;
		Payment payment = Payment.builder()
				.id(paymentId)
				.build();

		when(customerRepository.getReferenceById(CUSTOMER_ID)).thenReturn(customer);
		when(paymentService.findByIdAndCustomerAndDeletedAtIsNull(paymentId, customer)).thenReturn(payment);
		doNothing().when(paymentService).deletePayment(payment);

		// When
		customerService.deletePayment(CUSTOMER_ID, paymentId);

		// Then
		verify(customerRepository).getReferenceById(CUSTOMER_ID);
		verify(paymentService).findByIdAndCustomerAndDeletedAtIsNull(paymentId, customer);
		verify(paymentService).deletePayment(payment);
	}
}
