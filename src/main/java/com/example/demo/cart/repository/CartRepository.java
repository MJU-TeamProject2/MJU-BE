package com.example.demo.cart.repository;

import com.example.demo.cart.entity.Cart;
import com.example.demo.clothes.entity.Clothes;
import com.example.demo.customer.entity.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

  List<Cart> findByCustomer(Customer customer);
  Optional<Cart> findByCustomerAndClothes(Customer customer, Clothes clothes);
}
