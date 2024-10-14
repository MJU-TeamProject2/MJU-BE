package com.example.demo.cart.service;

import com.example.demo.cart.dto.request.AddToCartItemRequest;
import com.example.demo.cart.dto.request.UpdateCartItemRequest;
import com.example.demo.cart.dto.response.GetCartResponse;
import com.example.demo.cart.entity.Cart;
import com.example.demo.cart.repository.CartRepository;
import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.service.ClothesService;
import com.example.demo.customer.entity.Customer;

import com.example.demo.customer.service.CustomerService;
import com.example.demo.exception.CartNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {
  private final CartRepository cartRepository;
  private final CustomerService customerService;
  private final ClothesService clothesService;

  @Transactional(readOnly = true)
  public List<GetCartResponse> getCartItems(Long customerId) {
    Customer customer = customerService.findById(customerId);
    List<Cart> carts = cartRepository.findByCustomer(customer);

    return GetCartResponse.listOf(carts);
  }

  @Transactional
  public void addToCart(Long customerId, AddToCartItemRequest addToCartItemRequest) {
    Customer customer = customerService.findById(customerId);
    Clothes clothes = clothesService.findById(addToCartItemRequest.clothesId());
    cartRepository.findByCustomerAndClothes(customer, clothes)
        .ifPresentOrElse(
            this::incrementQuantity,
            () -> addToNewCart(customer, clothes)
        );
  }

  @Transactional
  public void updateCartItem(Long customerId, UpdateCartItemRequest updateCartItemRequest) {
    Customer customer = customerService.findById(customerId);
    Clothes clothes = clothesService.findById(updateCartItemRequest.clothesId());
    Cart cart = findByCustomerAndClothes(customer, clothes);
    cart.updateQuantity(updateCartItemRequest.quantity());
    cartRepository.save(cart);
  }

  @Transactional
  public void deleteFromCart(Long customerId, Long clothesId) {
    Customer customer = customerService.findById(customerId);
    Clothes clothes = clothesService.findById(clothesId);
    Cart cart = findByCustomerAndClothes(customer, clothes);
    cartRepository.delete(cart);
  }

  public Cart findByCustomerAndClothes(Customer customer, Clothes clothes) {
    return cartRepository.findByCustomerAndClothes(customer, clothes)
        .orElseThrow(CartNotFoundException::new);
  }

  public void incrementQuantity(Cart cart) {
    cart.updateQuantity(cart.getQuantity() + 1);
    cartRepository.save(cart);
  }

  public void addToNewCart(Customer customer, Clothes clothes) {
    cartRepository.save(
        Cart.builder()
            .customer(customer)
            .clothes(clothes)
            .build()
    );
  }
}
