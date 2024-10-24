package com.example.demo.customer.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.security.TokenProvider;
import com.example.demo.common.util.auth.Auth;
import com.example.demo.common.util.auth.AuthService;
import com.example.demo.customer.dto.request.AddAddressRequest;
import com.example.demo.customer.dto.request.AddPaymentRequest;
import com.example.demo.customer.dto.request.ProfileUpdateRequest;
import com.example.demo.customer.dto.request.UpdateAddressRequest;
import com.example.demo.customer.dto.request.UpdatePaymentRequest;
import com.example.demo.customer.dto.response.GetAddressDetailResponse;
import com.example.demo.customer.dto.response.GetAddressResponse;
import com.example.demo.customer.dto.response.GetCustomerResponse;
import com.example.demo.customer.dto.response.GetPaymentDetailResponse;
import com.example.demo.customer.dto.response.GetPaymentResponse;
import com.example.demo.customer.dto.response.LoginResponse;
import com.example.demo.customer.entity.Address;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.entity.Payment;
import com.example.demo.customer.repository.CustomerRepository;
import com.example.demo.exception.CustomerAuthNotFoundException;
import com.example.demo.exception.CustomerErrorCode;
import com.example.demo.exception.CustomerNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
	private final CustomerRepository customerRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenProvider tokenProvider;
	private final AuthService authService;
	private final AddressService addressService;
	private final PaymentService paymentService;

	public void register(Customer customer) {
		Customer newCustomer = customerRepository.save(customer);
		authService.createAuth(newCustomer.getEmail());
	}

	public void checkEmailDuplicate(String email) {
		if(customerRepository.findByEmail(email).isPresent()) {
			throw new CustomException(CustomerErrorCode.CUSTOMER_EMAIL_DUPLICATE);
		}
	}

	@Transactional
	public LoginResponse login(String email, String password) {
		Customer customer = customerRepository.findByEmail(email)
			.orElseThrow(CustomerNotFoundException::new);
		if (!passwordEncoder.matches(password, customer.getPassword())) {
			throw new CustomException(CustomerErrorCode.CUSTOMER_WRONG_PASSWORD);
		}

		String accessToken = tokenProvider.createAccessToken(customer.getId(), customer.getRole());
		String refreshToken = tokenProvider.createRefreshToken(customer.getId(), customer.getRole());

		Auth auth = authService.findByCode(customer.getEmail());
		auth.updateRefreshToken(refreshToken);

		return LoginResponse.builder()
			.customerId(customer.getId())
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.role(customer.getRole())
			.build();
	}

	@Transactional(readOnly = true)
	public GetCustomerResponse retrieveProfile(Long customerId) {
		return GetCustomerResponse.from(customerRepository.findById(customerId).orElseThrow(
			CustomerAuthNotFoundException::new));
	}

	@Transactional
	public void updateProfile(Long customerId, ProfileUpdateRequest profileUpdateRequest) {
		Customer customer = customerRepository.getReferenceById(customerId);
		authService.update(customer.getEmail(), profileUpdateRequest.email());
		customer.update(profileUpdateRequest.email(), profileUpdateRequest.name(), profileUpdateRequest.nickName(),
			profileUpdateRequest.age(), profileUpdateRequest.phoneNumber());
	}

	public Customer findById(Long customerId) {
		return customerRepository.findById(customerId).orElseThrow(CustomerNotFoundException::new);
	}

	@Transactional(readOnly = true)
	public List<GetAddressResponse> getAddresses(Long customerId) {
		Customer customer = customerRepository.getReferenceById(customerId);
		List<Address> addresses = addressService.findByCustomerAndDeletedAtIsNull(customer);
		return GetAddressResponse.listOf(addresses);
	}

	@Transactional(readOnly = true)
	public GetAddressDetailResponse getAddressDetail(Long customerId, Long addressId) {
		Customer customer = customerRepository.getReferenceById(customerId);
		return GetAddressDetailResponse.from(
				addressService.findByIdAndCustomerAndDeletedAtIsNull(addressId, customer)
		);
	}

	@Transactional
	public void addAddress(Long customerId, AddAddressRequest addAddressRequest) {
		Customer customer = customerRepository.getReferenceById(customerId);
		addressService.addAddress(customer, addAddressRequest);
	}

	@Transactional
	public void updateAddress(Long customerId, UpdateAddressRequest updateAddressRequest) {
		Customer customer = customerRepository.getReferenceById(customerId);
		Address address = addressService.findByIdAndCustomerAndDeletedAtIsNull(updateAddressRequest.addressId(), customer);
		addressService.updateAddress(address, updateAddressRequest);
	}

	@Transactional
	public void deleteAddress(Long customerId, Long addressId) {
		Customer customer = customerRepository.getReferenceById(customerId);
		Address address = addressService.findByIdAndCustomerAndDeletedAtIsNull(addressId, customer);
		addressService.deleteAddress(address);
	}

	@Transactional(readOnly = true)
	public List<GetPaymentResponse> getPayments(Long customerId) {
		Customer customer = customerRepository.getReferenceById(customerId);
		List<Payment> payments = paymentService.findByCustomerAndDeletedAtIsNull(customer);
		return GetPaymentResponse.listOf(payments);
	}

	@Transactional(readOnly = true)
	public GetPaymentDetailResponse getPaymentDetail(Long customerId, Long paymentId) {
		Customer customer = customerRepository.getReferenceById(customerId);
		return GetPaymentDetailResponse.from(
				paymentService.findByIdAndCustomerAndDeletedAtIsNull(paymentId, customer)
		);
	}

	@Transactional
	public void addPayment(Long customerId, AddPaymentRequest addPaymentRequest) {
		Customer customer = customerRepository.getReferenceById(customerId);
		paymentService.addPayment(customer, addPaymentRequest);
	}

	@Transactional
	public void updatePayment(Long customerId, UpdatePaymentRequest updatePaymentRequest) {
		Customer customer = customerRepository.getReferenceById(customerId);
		Payment payment = paymentService.findByIdAndCustomerAndDeletedAtIsNull(updatePaymentRequest.paymentId(), customer);
		paymentService.updatePayment(payment, updatePaymentRequest);
	}

	@Transactional
	public void deletePayment(Long customerId, Long paymentId) {
		Customer customer = customerRepository.getReferenceById(customerId);
		Payment payment = paymentService.findByIdAndCustomerAndDeletedAtIsNull(paymentId, customer);
		paymentService.deletePayment(payment);
	}
}
