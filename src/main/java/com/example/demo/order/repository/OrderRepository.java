package com.example.demo.order.repository;


import com.example.demo.customer.entity.Customer;
import com.example.demo.order.entity.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByCustomer(Customer customer);
  Optional<Order> findOneByIdAndCustomer(Long orderId, Customer customer);
}
