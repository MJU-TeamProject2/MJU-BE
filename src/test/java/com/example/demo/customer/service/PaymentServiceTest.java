package com.example.demo.customer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.demo.customer.entity.BodyType;
import com.example.demo.customer.entity.CardProvider;
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

import com.example.demo.customer.dto.request.AddPaymentRequest;
import com.example.demo.customer.dto.request.UpdatePaymentRequest;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.entity.Payment;
import com.example.demo.customer.repository.PaymentRepository;
import com.example.demo.exception.CustomerPaymentNotFoundExeption;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, TestResultLogger.class})
@Slf4j
class PaymentServiceTest {

  @InjectMocks
  private PaymentService paymentService;

  @Mock
  private PaymentRepository paymentRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    paymentService = new PaymentService(paymentRepository);
  }

  @Test
  @DisplayName("고객의 결제 수단을 ID로 조회할 수 있다")
  void 고객의_결제수단을_조회한다() {
    // given
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Payment payment = Payment.builder()
        .id(1L)
        .customer(customer)
        .cardNumber("1234-5678-9012-3456")
        .cardProvider(CardProvider.LOTTE)
        .build();

    given(paymentRepository.findOneByIdAndCustomer(1L, customer))
        .willReturn(Optional.of(payment));

    // when
    Payment result = paymentService.findOneByIdAndCustomer(1L, customer);

    // then
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getCardNumber()).isEqualTo("1234-5678-9012-3456");
    assertThat(result.getCardProvider()).isEqualTo(CardProvider.LOTTE);
  }

  @Test
  @DisplayName("삭제되지 않은 고객의 결제 수단 목록을 조회할 수 있다")
  void 삭제되지_않은_고객의_결제수단_목록을_조회한다() {
    // given
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Payment payment = Payment.builder()
        .id(1L)
        .customer(customer)
        .deletedAt(null)
        .build();

    given(paymentRepository.findByCustomerAndDeletedAtIsNull(customer))
        .willReturn(List.of(payment));

    // when
    List<Payment> results = paymentService.findByCustomerAndDeletedAtIsNull(customer);

    // then
    assertThat(results).hasSize(1);
    assertThat(results.get(0).getId()).isEqualTo(1L);
  }

  @Test
  @DisplayName("존재하지 않는 결제 수단을 조회하면 예외가 발생한다")
  void 존재하지_않는_결제수단을_조회할_수_없다() {
    // given
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    given(paymentRepository.findOneByIdAndCustomer(1L, customer))
        .willReturn(Optional.empty());

    // when & then
    assertThrows(CustomerPaymentNotFoundExeption.class,
        () -> paymentService.findOneByIdAndCustomer(1L, customer));
  }

  @Test
  @DisplayName("새로운 결제 수단을 추가할 수 있다")
  void 새로운_결제수단을_추가한다() {
    // given
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    AddPaymentRequest request = new AddPaymentRequest(
        "1234-5678-9012-3456",
        CardProvider.LOTTE,
        "12/25"
    );

    // when
    paymentService.addPayment(customer, request);

    // then
    verify(paymentRepository).save(any(Payment.class));
  }

  @Test
  @DisplayName("기존 결제 수단을 수정할 수 있다")
  void 결제수단을_수정한다() {
    // given
    Payment payment = Payment.builder()
        .id(1L)
        .cardNumber("1234-5678-9012-3456")
        .cardProvider(CardProvider.LOTTE)
        .expiryDate("12/25")
        .build();

    UpdatePaymentRequest request = new UpdatePaymentRequest(
        1L,
        "9876-5432-1098-7654",
        CardProvider.LOTTE,
        "06/28"
    );

    // when
    paymentService.updatePayment(payment, request);

    // then
    assertThat(payment.getCardNumber()).isEqualTo("9876-5432-1098-7654");
    assertThat(payment.getCardProvider()).isEqualTo(CardProvider.LOTTE);
    assertThat(payment.getExpiryDate()).isEqualTo("06/28");
    verify(paymentRepository).save(payment);
  }

  @Test
  @DisplayName("결제 수단을 soft delete로 삭제할 수 있다")
  void 결제수단을_삭제한다() {
    // given
    Payment payment = Payment.builder()
        .id(1L)
        .build();

    // when
    paymentService.deletePayment(payment);

    // then
    verify(paymentRepository).softDelete(any(Payment.class), any(LocalDateTime.class));
  }

  @Test
  @DisplayName("삭제되지 않은 결제 수단을 ID와 고객 정보로 조회할 수 있다")
  void 삭제되지_않은_결제수단을_ID로_조회한다() {
    // given
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Payment payment = Payment.builder()
        .id(1L)
        .customer(customer)
        .deletedAt(null)
        .build();

    given(paymentRepository.findByIdAndCustomerAndDeletedAtIsNull(1L, customer))
        .willReturn(Optional.of(payment));

    // when
    Payment result = paymentService.findByIdAndCustomerAndDeletedAtIsNull(1L, customer);

    // then
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getDeletedAt()).isNull();
  }
}
