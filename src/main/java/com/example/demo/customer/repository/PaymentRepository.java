package com.example.demo.customer.repository;

import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.entity.Payment;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
  Optional<Payment> findOneByIdAndCustomer(Long addressId, Customer customer);
  List<Payment> findByCustomerAndDeletedAtIsNull(Customer customer);
  Optional<Payment> findByIdAndCustomerAndDeletedAtIsNull(Long id, Customer customer);

  @Modifying
  @Query("UPDATE Payment a SET a.deletedAt = :deletedAt WHERE a = :payment")
  void softDelete(@Param("payment") Payment payment, @Param("deletedAt") LocalDateTime deletedAt);

}
