package com.example.demo.customer.service;

import com.example.demo.customer.dto.request.AddPaymentRequest;
import com.example.demo.customer.dto.request.UpdatePaymentRequest;
import com.example.demo.customer.entity.Address;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.entity.Payment;
import com.example.demo.customer.repository.PaymentRepository;
import com.example.demo.exception.CustomerPaymentNotFoundExeption;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository paymentRepository;

  public Payment findOneByIdAndCustomer(Long paymentId, Customer customer) {
    return paymentRepository.findOneByIdAndCustomer(paymentId, customer)
        .orElseThrow(CustomerPaymentNotFoundExeption::new);
  }

  public List<Payment> findByCustomerAndDeletedAtIsNull(Customer customer) {
    return paymentRepository.findByCustomerAndDeletedAtIsNull(customer);
  }

  public Payment findByIdAndCustomerAndDeletedAtIsNull(Long paymentId, Customer customer) {
    return paymentRepository.findByIdAndCustomerAndDeletedAtIsNull(paymentId, customer)
        .orElseThrow(CustomerPaymentNotFoundExeption::new);
  }

  public void addPayment(Customer customer, AddPaymentRequest addPaymentRequest) {
    paymentRepository.save(
        Payment.builder()
            .customer(customer)
            .cardNumber(addPaymentRequest.cardNumber())
            .cardProvider(addPaymentRequest.cardProvider())
            .expiryDate(addPaymentRequest.expiryDate())
            .build()
    );
  }

  public void updatePayment(Payment payment, UpdatePaymentRequest updatePaymentRequest) {
    payment.update(
        updatePaymentRequest.cardNumber(),
        updatePaymentRequest.cardProvider(),
        updatePaymentRequest.expiryDate()
    );
    paymentRepository.save(payment);
  }

  public void deletePayment(Payment payment) {
    paymentRepository.softDelete(payment, LocalDateTime.now());
  }
}
