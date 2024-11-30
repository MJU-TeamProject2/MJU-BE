package com.example.demo.customer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.demo.customer.entity.BodyType;
import com.example.demo.customer.entity.Gender;
import com.example.demo.util.TestResultLogger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.customer.dto.request.AddAddressRequest;
import com.example.demo.customer.dto.request.UpdateAddressRequest;
import com.example.demo.customer.entity.Address;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.repository.AddressRepository;
import com.example.demo.exception.CustomerAddressNotFoundException;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, TestResultLogger.class})
@Slf4j
class AddressServiceTest {

  @InjectMocks
  private AddressService addressService;

  @Mock
  private AddressRepository addressRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    addressService = new AddressService(addressRepository);
  }

  @Test
  @DisplayName("고객의 주소를 ID로 조회할 수 있다")
  void 고객의_주소를_조회한다() {
    // given
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Address address = Address.builder()
        .id(1L)
        .customer(customer)
        .name("집")
        .recipient("홍길동")
        .build();

    given(addressRepository.findOneByIdAndCustomer(1L, customer))
        .willReturn(Optional.of(address));

    // when
    Address result = addressService.findOneByIdAndCustomer(1L, customer);

    // then
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getName()).isEqualTo("집");
    assertThat(result.getRecipient()).isEqualTo("홍길동");
  }

  @Test
  @DisplayName("삭제되지 않은 고객의 주소 목록을 조회할 수 있다")
  void 삭제되지_않은_고객의_주소_목록을_조회한다() {
    // given
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Address address = Address.builder()
        .id(1L)
        .customer(customer)
        .deletedAt(null)
        .build();

    given(addressRepository.findByCustomerAndDeletedAtIsNull(customer))
        .willReturn(List.of(address));

    // when
    List<Address> results = addressService.findByCustomerAndDeletedAtIsNull(customer);

    // then
    assertThat(results).hasSize(1);
    assertThat(results.get(0).getId()).isEqualTo(1L);
  }

  @Test
  @DisplayName("존재하지 않는 주소를 조회하면 예외가 발생한다")
  void 존재하지_않는_주소를_조회할_수_없다() {
    // given
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    given(addressRepository.findOneByIdAndCustomer(1L, customer))
        .willReturn(Optional.empty());

    // when & then
    assertThrows(CustomerAddressNotFoundException.class,
        () -> addressService.findOneByIdAndCustomer(1L, customer));
  }

  @Test
  @DisplayName("새로운 주소를 추가할 수 있다")
  void 새로운_주소를_추가한다() {
    // given
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    AddAddressRequest request = new AddAddressRequest(
        "집",
        "홍길동",
        "12345",
        "서울시 강남구",
        "101동 101호"
    );

    // when
    addressService.addAddress(customer, request);

    // then
    verify(addressRepository).save(any(Address.class));
  }

  @Test
  @DisplayName("기존 주소를 수정할 수 있다")
  void 주소를_수정한다() {
    // given
    Address address = Address.builder()
        .id(1L)
        .name("집")
        .recipient("홍길동")
        .zipCode("12345")
        .baseAddress("서울시 강남구")
        .detailAddress("101동 101호")
        .build();

    UpdateAddressRequest request = new UpdateAddressRequest(
        1L,
        "회사",
        "홍길동",
        "54321",
        "서울시 서초구",
        "202동 202호"
    );

    // when
    addressService.updateAddress(address, request);

    // then
    assertThat(address.getName()).isEqualTo("회사");
    assertThat(address.getZipCode()).isEqualTo("54321");
    assertThat(address.getBaseAddress()).isEqualTo("서울시 서초구");
    assertThat(address.getDetailAddress()).isEqualTo("202동 202호");
    verify(addressRepository).save(address);
  }

  @Test
  @DisplayName("주소를 soft delete로 삭제할 수 있다")
  void 주소를_삭제한다() {
    // given
    Address address = Address.builder()
        .id(1L)
        .build();

    // when
    addressService.deleteAddress(address);

    // then
    verify(addressRepository).softDelete(any(Address.class), any(LocalDateTime.class));
  }

  @Test
  @DisplayName("삭제되지 않은 주소를 ID와 고객 정보로 조회할 수 있다")
  void 삭제되지_않은_주소를_ID로_조회한다() {
    // given
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Address address = Address.builder()
        .id(1L)
        .customer(customer)
        .deletedAt(null)
        .build();

    given(addressRepository.findByIdAndCustomerAndDeletedAtIsNull(1L, customer))
        .willReturn(Optional.of(address));

    // when
    Address result = addressService.findByIdAndCustomerAndDeletedAtIsNull(1L, customer);

    // then
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getDeletedAt()).isNull();
  }
}
