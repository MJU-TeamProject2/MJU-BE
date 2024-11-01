package com.example.demo.cart.service.application;

import com.example.demo.cart.dto.request.AddToCartItemRequest;
import com.example.demo.cart.dto.request.UpdateCartItemRequest;
import com.example.demo.cart.dto.response.GetCartResponse;
import com.example.demo.cart.entity.Cart;
import com.example.demo.cart.service.CartService;
import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.entity.ClothesSize;
import com.example.demo.clothes.service.ClothesService;
import com.example.demo.clothes.service.ClothesSizeService;
import com.example.demo.common.util.S3Service;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.service.CustomerService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartApplicationService {
  private final CartService cartService;
  private final CustomerService customerService;
  private final ClothesService clothesService;
  private final ClothesSizeService clothesSizeService;
  private final S3Service s3Service;

  @Transactional(readOnly = true)
  public List<GetCartResponse> getCartItems(Long customerId) {
    Customer customer = customerService.findById(customerId);
    return cartService.findByCustomer(customer).stream()
        .map(cart -> GetCartResponse.from(cart,
            s3Service.generatePresignedUrl(cart.getClothes().getImageUrl()),
            s3Service.generatePresignedUrl(cart.getClothes().getDetailUrl())))
        .toList();
  }

  @Transactional
  public void addToCart(Long customerId, AddToCartItemRequest request) {
    Customer customer = customerService.findById(customerId);
    Clothes clothes = clothesService.findById(request.clothesId());
    ClothesSize clothesSize = clothesSizeService.findByClothesIdAndSize(clothes, request.size());
    cartService.addToCart(customer, clothes, request.quantity(), clothesSize);
  }

  @Transactional
  public void updateCartItem(Long customerId, UpdateCartItemRequest request) {
    Cart cart = cartService.findByIdAndCustomerId(request.cartId(), customerId);
    cartService.updateCartItem(cart, request.quantity());
  }

  @Transactional
  public void deleteFromCart(Long customerId, Long cartId) {
    Cart cart = cartService.findByIdAndCustomerId(cartId, customerId);
    cartService.deleteFromCart(cart);
  }
}
