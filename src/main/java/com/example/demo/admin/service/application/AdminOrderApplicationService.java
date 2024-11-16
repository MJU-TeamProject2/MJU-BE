package com.example.demo.admin.service.application;

import com.example.demo.admin.dto.request.AdminUpdateOrderRequest;
import com.example.demo.admin.dto.response.AdminGetOrderDetailResponse;
import com.example.demo.admin.dto.response.AdminGetOrderResponse;
import com.example.demo.common.util.S3Service;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.service.CustomerService;
import com.example.demo.order.entity.Order;
import com.example.demo.order.service.OrderService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderApplicationService {

  private final OrderService orderService;
  private final S3Service s3Service;
  private final CustomerService customerService;

  @Transactional(readOnly = true)
  public List<AdminGetOrderResponse> getOrders(Long memberId, PageRequest pageRequest) {
    Page<Order> orders;
    if (memberId != null) {
      Customer customer = customerService.findById(memberId);
      orders = orderService.findByCustomer(customer, pageRequest);
    } else {
      orders = orderService.findAll(pageRequest);
    }

    return orders.getContent().stream()
        .map(order -> AdminGetOrderResponse.from(
            order,
            s3Service.generatePresignedUrl(order.getClothes().getImageUrl()),
            s3Service.generatePresignedUrl(order.getClothes().getDetailUrl())
        ))
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public AdminGetOrderDetailResponse getOrder(Long orderId) {
    Order order = orderService.findById(orderId);
    return AdminGetOrderDetailResponse.from(
        order,
        s3Service.generatePresignedUrl(order.getClothes().getImageUrl()),
        s3Service.generatePresignedUrl(order.getClothes().getDetailUrl())
    );
  }

  @Transactional
  public void updateOrder(AdminUpdateOrderRequest adminUpdateOrderRequest) {
    Order order = orderService.findById(adminUpdateOrderRequest.orderId());
    order.updateOrderStatus(adminUpdateOrderRequest.orderStatus());
    orderService.save(order);
  }
}
