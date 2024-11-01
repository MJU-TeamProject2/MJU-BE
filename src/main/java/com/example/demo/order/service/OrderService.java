package com.example.demo.order.service;

import com.example.demo.cart.entity.Cart;
import com.example.demo.customer.entity.Address;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.entity.Payment;
import com.example.demo.exception.ClothesInsufficientStockException;
import com.example.demo.order.entity.Order;
import com.example.demo.order.entity.OrderStatus;
import com.example.demo.order.exception.OrderNotFoundException;
import com.example.demo.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  public List<Order> findByCustomer(Customer customer) {
    return orderRepository.findByCustomer(customer);
  }

  public Order findOneByIdAndCustomer(Long orderId, Customer customer) {
    return orderRepository.findOneByIdAndCustomer(orderId, customer)
        .orElseThrow(OrderNotFoundException::new);
  }

  public void addToOrderItem(Customer customer, Cart cart, Address address, Payment payment) {
    if (cart.getQuantity() > cart.getClothesSize().getQuantity()) {
      throw new ClothesInsufficientStockException();
    }

    orderRepository.save(
        Order.builder()
            .customer(customer)
            .clothes(cart.getClothes())
            .clothesSize(cart.getClothesSize())
            .address(address)
            .payment(payment)
            .status(OrderStatus.PREPARING)
            .quantity(cart.getQuantity())
            .build()
    );
  }

  public Order findById(Long orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(OrderNotFoundException::new);
  }

  public void save(Order order) {
    orderRepository.save(order);
  }

  public void deleteFromOrder(Order order) {
    orderRepository.delete(order);
  }

  public Page<Order> findAll(Pageable pageable) {
    return orderRepository.findAll(pageable);
  }

  public Page<Order> findByCustomer(Customer customer, Pageable pageable) {
    return orderRepository.findByCustomer(customer, pageable);
  }
}
