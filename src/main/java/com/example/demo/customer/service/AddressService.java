package com.example.demo.customer.service;

import com.example.demo.customer.dto.request.AddAddressRequest;
import com.example.demo.customer.dto.request.UpdateAddressRequest;
import com.example.demo.customer.entity.Address;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.repository.AddressRepository;
import com.example.demo.exception.CustomerAddressNotFoundException;
import java.time.LocalDateTime;
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

  public Address findByIdAndCustomerAndDeletedAtIsNull(Long addressId, Customer customer) {
    return addressRepository.findByIdAndCustomerAndDeletedAtIsNull(addressId, customer)
        .orElseThrow(CustomerAddressNotFoundException::new);
  }

  public void addAddress(Customer customer, AddAddressRequest addAddressRequest) {
    addressRepository.save(
        Address.builder()
            .customer(customer)
            .name(addAddressRequest.name())
            .recipient(addAddressRequest.recipient())
            .zipCode(addAddressRequest.zipCode())
            .baseAddress(addAddressRequest.baseAddress())
            .detailAddress(addAddressRequest.detailAddress())
            .build()
    );
  }

  public void updateAddress(Address address, UpdateAddressRequest updateAddressRequest) {
    address.update(
        updateAddressRequest.name(),
        updateAddressRequest.recipient(),
        updateAddressRequest.zipCode(),
        updateAddressRequest.baseAddress(),
        updateAddressRequest.detailAddress()
    );
    addressRepository.save(address);
  }

  public void deleteAddress(Address address) {
    addressRepository.softDelete(address, LocalDateTime.now());
  }
}
