package com.example.demo.cart.entity;

import com.example.demo.customer.entity.Customer;
import com.example.demo.clothes.entity.Clothes;
import com.example.demo.common.util.BaseEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cart_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @ManyToOne
  @JoinColumn(name = "clothes_id")
  private Clothes clothes;

  @Builder.Default
  private Integer quantity = 1;

  public void updateQuantity(Integer newQuantity) {
    this.quantity = newQuantity;
  }
}
