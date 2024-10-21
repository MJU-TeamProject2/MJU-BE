package com.example.demo.cart.service;

import com.example.demo.cart.entity.Cart;
import com.example.demo.cart.exception.AlreadyExsistsCartException;
import com.example.demo.cart.repository.CartRepository;
import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.entity.ClothesSize;
import com.example.demo.customer.entity.Customer;
import com.example.demo.cart.exception.CartNotFoundException;
import com.example.demo.exception.ClothesInsufficientStockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
  private final CartRepository cartRepository;

  public List<Cart> findByCustomer(Customer customer) {
    return cartRepository.findByCustomer(customer);
  }

  public void addToCart(Customer customer, Clothes clothes, int quantity, ClothesSize clothesSize) {
    cartRepository.findByCustomerAndClothesAndClothesSize(customer, clothes, clothesSize)
        .ifPresentOrElse(
            cart -> updateCartQuantity(cart, quantity),
            () -> addToNewCart(customer, clothes, quantity, clothesSize)
        );
  }

  public void updateCartItem(Cart cart, int quantity, ClothesSize clothesSize) {
    cartRepository.findByCustomerAndClothesAndClothesSize(cart.getCustomer(), cart.getClothes(), cart.getClothesSize())
        .ifPresentOrElse(
          item -> {
            throw new AlreadyExsistsCartException();
          },
          () -> {
            cart.updateClothesSize(clothesSize);
            cart.updateQuantity(quantity);
            cartRepository.save(cart);
          }
        );
  }


  public void deleteFromCart(Cart cart) {
    cartRepository.delete(cart);
  }

  private void updateCartQuantity(Cart cart, int requestedQuantity) {
    int newQuantity = cart.getQuantity() + requestedQuantity;
    cart.updateQuantity(newQuantity);
    cart.getClothesSize().isQuantityAvailable(newQuantity);
    cartRepository.save(cart);
  }

  private void addToNewCart(Customer customer, Clothes clothes, int quantity, ClothesSize clothesSize) {
    if (quantity > clothesSize.getQuantity()) {
      throw new ClothesInsufficientStockException();
    }
    cartRepository.save(
        Cart.builder()
            .customer(customer)
            .clothes(clothes)
            .quantity(quantity)
            .clothesSize(clothesSize)
            .build()
    );
  }

  public Cart findByIdAndCustomerId(Long cartId, Long customerId) {
    return cartRepository.findByIdAndCustomerId(cartId, customerId)
        .orElseThrow(CartNotFoundException::new);
  }
}