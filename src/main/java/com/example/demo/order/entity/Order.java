package com.example.demo.order.entity;

import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.entity.ClothesSize;
import com.example.demo.common.util.BaseEntity;
import com.example.demo.customer.entity.Address;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.entity.Payment;
import com.example.demo.order.exception.OrderStatusNotValidException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "orders")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @ManyToOne
  @JoinColumn(name = "clothes_id")
  private Clothes clothes;

  @ManyToOne
  @JoinColumn(name = "clothes_size_id")
  private ClothesSize clothesSize;

  @ManyToOne
  @JoinColumn(name = "address_id")
  private Address address;

  @ManyToOne
  @JoinColumn(name = "payment_id")
  private Payment payment;

  private Integer quantity;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  private LocalDateTime deletedAt;

  public void updateAddress(Address address) {
    if (this.status == OrderStatus.PREPARING) {
      this.address = address;
    } else {
      throw new OrderStatusNotValidException();
    }
  }

  public void updateOrderStatus(OrderStatus orderStatus) {
    this.status = orderStatus;
  }
}
