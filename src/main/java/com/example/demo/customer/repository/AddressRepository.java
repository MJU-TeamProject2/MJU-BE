package com.example.demo.customer.repository;

import com.example.demo.customer.entity.Address;
import com.example.demo.customer.entity.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
  Optional<Address> findOneByIdAndCustomer(Long addressId, Customer customer);
}
