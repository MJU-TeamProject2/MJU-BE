package com.example.demo.customer.repository;

import com.example.demo.customer.entity.Address;
import com.example.demo.customer.entity.Customer;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
  Optional<Address> findOneByIdAndCustomer(Long addressId, Customer customer);
  List<Address> findByCustomerAndDeletedAtIsNull(Customer customer);
  Optional<Address> findByIdAndCustomerAndDeletedAtIsNull(Long id, Customer customer);

  @Modifying
  @Query("UPDATE Address a SET a.deletedAt = :deletedAt WHERE a = :address")
  void softDelete(@Param("address") Address address, @Param("deletedAt") LocalDateTime deletedAt);
}
