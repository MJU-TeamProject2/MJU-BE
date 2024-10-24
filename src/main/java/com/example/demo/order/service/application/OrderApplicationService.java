package com.example.demo.order.service.application;

import com.example.demo.cart.entity.Cart;
import com.example.demo.cart.service.CartService;
import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.service.ClothesSizeService;
import com.example.demo.common.util.S3Service;
import com.example.demo.customer.entity.Address;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.entity.Payment;
import com.example.demo.customer.service.AddressService;
import com.example.demo.customer.service.CustomerService;
import com.example.demo.customer.service.PaymentService;
import com.example.demo.order.dto.request.AddToOrderItemRequest;
import com.example.demo.order.dto.request.UpdateOrderItemRequest;
import com.example.demo.order.dto.response.GetOrderDetailResponse;
import com.example.demo.order.dto.response.GetOrderResponse;
import com.example.demo.order.entity.Order;
import com.example.demo.order.service.OrderService;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderApplicationService {

  private final OrderService orderService;
  private final CustomerService customerService;
  private final CartService cartService;
  private final AddressService addressService;
  private final PaymentService paymentService;
  private final ClothesSizeService clothesSizeService;
  private final S3Service s3Service;

  @Transactional(readOnly = true)
  public List<GetOrderResponse> getOrderItems(Long customerId) {
    Customer customer = customerService.findById(customerId);
    List<Order> orders = orderService.findByCustomer(customer);
    return GetOrderResponse.listOf(
        orders.stream()
            .map(order -> {
              Clothes clothes = order.getClothes();
              clothes.setImageUrl(s3Service.generatePresignedUrl(clothes.getImageUrl()));
              clothes.setDetailUrl(s3Service.generatePresignedUrl(clothes.getDetailUrl()));
              return order;
            })
            .collect(Collectors.toList())
    );
  }

  @Transactional(readOnly = true)
  public GetOrderDetailResponse getOrderItemDetail(Long customerId, Long orderId) {
    Customer customer = customerService.findById(customerId);
    Order order = orderService.findOneByIdAndCustomer(orderId, customer);
    Clothes clothes = order.getClothes();
    clothes.setImageUrl(s3Service.generatePresignedUrl(clothes.getImageUrl()));
    clothes.setDetailUrl(s3Service.generatePresignedUrl(clothes.getDetailUrl()));
    return GetOrderDetailResponse.from(order);
  }

  @Transactional
  public void addToOrderItem(Long customerId, @Valid AddToOrderItemRequest addToOrderItemRequest) {
    Cart cart = cartService.findByIdAndCustomerId(addToOrderItemRequest.cartId(), customerId);
    Customer customer = customerService.findById(customerId);
    Address address = addressService.findOneByIdAndCustomer(addToOrderItemRequest.AddressId(), customer);
    Payment payment = paymentService.findOneByIdAndCustomer(addToOrderItemRequest.paymentId(), customer);

    orderService.addToOrderItem(customer, cart, address, payment);
    clothesSizeService.updateClothesQuantity(cart.getClothesSize(), cart.getClothesSize().getQuantity() - cart.getQuantity());
    cartService.deleteFromCart(cart);
  }

  @Transactional
  public void updateOrderItem(Long customerId, @Valid UpdateOrderItemRequest updateOrderItemRequest) {
    Customer customer = customerService.findById(customerId);
    Order order = orderService.findOneByIdAndCustomer(updateOrderItemRequest.orderId(), customer);
    Address address = addressService.findOneByIdAndCustomer(updateOrderItemRequest.addressId(), customer);
    order.updateAddress(address);
    orderService.save(order);
  }

  @Transactional
  public void deleteOrderItem(Long customerId, Long orderId) {
    Customer customer = customerService.findById(customerId);
    Order order = orderService.findOneByIdAndCustomer(orderId, customer);
    orderService.deleteFromOrder(order);
    clothesSizeService.updateClothesQuantity(order.getClothesSize(), order.getClothesSize().getQuantity() + order.getQuantity());
  }
}
