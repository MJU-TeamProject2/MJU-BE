package com.example.demo.customer.repository;

import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.entity.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
  Optional<Payment> findOneByIdAndCustomer(Long addressId, Customer customer);
}
