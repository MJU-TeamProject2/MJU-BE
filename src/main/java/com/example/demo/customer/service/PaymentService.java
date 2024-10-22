package com.example.demo.customer.service;

import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.entity.Payment;
import com.example.demo.customer.repository.PaymentRepository;
import com.example.demo.exception.CustomerPaymentNotFoundExeption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository paymentRepository;

  public Payment findOneByIdAndCustomer(Long paymentId, Customer customer) {
    return paymentRepository.findOneByIdAndCustomer(paymentId, customer)
        .orElseThrow(CustomerPaymentNotFoundExeption::new);
  }
}
