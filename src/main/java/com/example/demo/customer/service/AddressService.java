package com.example.demo.customer.service;

import com.example.demo.customer.entity.Address;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.repository.AddressRepository;
import com.example.demo.exception.CustomerAddressNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

  private final AddressRepository addressRepository;

  public Address findOneByIdAndCustomer(Long addressId, Customer customer) {
    return addressRepository.findOneByIdAndCustomer(addressId, customer)
        .orElseThrow(CustomerAddressNotFoundException::new);
  }

  public List<Address> findByCustomerAndDeletedAtIsNull(Customer customer) {
    return addressRepository.findByCustomerAndDeletedAtIsNull(customer);
  }
}
