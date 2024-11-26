package com.example.demo.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.demo.admin.service.application.AdminOrderApplicationService;
import com.example.demo.clothes.entity.ClothesSize;
import com.example.demo.customer.entity.Address;
import com.example.demo.customer.entity.BodyType;
import com.example.demo.customer.entity.Gender;
import com.example.demo.customer.entity.Payment;
import com.example.demo.util.TestResultLogger;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.example.demo.admin.dto.request.AdminUpdateOrderRequest;
import com.example.demo.admin.dto.response.AdminGetOrderDetailResponse;
import com.example.demo.admin.dto.response.AdminGetOrderResponse;
import com.example.demo.common.util.S3Service;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.service.CustomerService;
import com.example.demo.order.entity.Order;
import com.example.demo.order.entity.OrderStatus;
import com.example.demo.order.service.OrderService;
import com.example.demo.clothes.entity.Clothes;

@ExtendWith({MockitoExtension.class, TestResultLogger.class})
@Slf4j
class AdminOrderServiceTest {

  @InjectMocks
  private AdminOrderApplicationService adminOrderApplicationService;

  @Mock
  private OrderService orderService;
  @Mock
  private S3Service s3Service;
  @Mock
  private CustomerService customerService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    adminOrderApplicationService = new AdminOrderApplicationService(
        orderService,
        s3Service,
        customerService
    );
  }

  @Test
  void 전체_주문_목록을_조회한다() {
    // given
    PageRequest pageRequest = PageRequest.of(0, 10);
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Clothes clothes = Clothes.builder()
        .imageUrl("image.jpg")
        .detailUrl("detail.jpg")
        .build();
    ClothesSize clothesSize = ClothesSize.builder().quantity(10).build();
    Order order = Order.builder()
        .id(1L)
        .clothes(clothes)
        .customer(customer)
        .clothesSize(clothesSize)
        .build();
    Page<Order> orderPage = new PageImpl<>(List.of(order));

    given(orderService.findAll(pageRequest)).willReturn(orderPage);
    given(s3Service.generatePresignedUrl(any())).willReturn("presigned-url");

    // when
    List<AdminGetOrderResponse> result = adminOrderApplicationService.getOrders(null, pageRequest);

    // then
    assertThat(result).hasSize(1);
    verify(orderService).findAll(pageRequest);
    verify(s3Service).generatePresignedUrl("image.jpg");
    verify(s3Service).generatePresignedUrl("detail.jpg");
  }

  @Test
  void 특정_고객의_주문_목록을_조회한다() {
    // given
    Long memberId = 1L;
    PageRequest pageRequest = PageRequest.of(0, 10);
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Clothes clothes = Clothes.builder()
        .imageUrl("image.jpg")
        .detailUrl("detail.jpg")
        .build();
    ClothesSize clothesSize = ClothesSize.builder().quantity(10).build();
    Order order = Order.builder()
        .id(1L)
        .clothes(clothes)
        .customer(customer)
        .clothesSize(clothesSize)
        .build();
    Page<Order> orderPage = new PageImpl<>(List.of(order));

    given(customerService.findById(memberId)).willReturn(customer);
    given(orderService.findByCustomer(customer, pageRequest)).willReturn(orderPage);
    given(s3Service.generatePresignedUrl(any())).willReturn("presigned-url");

    // when
    List<AdminGetOrderResponse> result = adminOrderApplicationService.getOrders(memberId, pageRequest);

    // then
    assertThat(result).hasSize(1);
    verify(customerService).findById(memberId);
    verify(orderService).findByCustomer(customer, pageRequest);
  }

  @Test
  void 주문_상세_정보를_조회한다() {
    // given
    Long orderId = 1L;
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Clothes clothes = Clothes.builder()
        .imageUrl("image.jpg")
        .detailUrl("detail.jpg")
        .build();
    ClothesSize clothesSize = ClothesSize.builder().quantity(10).build();
    Order order = Order.builder()
        .id(1L)
        .clothes(clothes)
        .customer(customer)
        .clothesSize(clothesSize)
        .payment(Payment.builder().id(1L).build())
        .address(Address.builder().id(1L).build())
        .build();

    given(orderService.findById(orderId)).willReturn(order);
    given(s3Service.generatePresignedUrl(any())).willReturn("presigned-url");

    // when
    AdminGetOrderDetailResponse result = adminOrderApplicationService.getOrder(orderId);

    // then
    assertThat(result).isNotNull();
    verify(orderService).findById(orderId);
    verify(s3Service).generatePresignedUrl("image.jpg");
    verify(s3Service).generatePresignedUrl("detail.jpg");
  }

  @Test
  void 주문_상태를_수정한다() {
    // given
    Long orderId = 1L;
    Order order = Order.builder()
        .id(orderId)
        .status(OrderStatus.PREPARING)
        .build();
    AdminUpdateOrderRequest request = new AdminUpdateOrderRequest(orderId, OrderStatus.SHIPPING);

    given(orderService.findById(orderId)).willReturn(order);

    // when
    adminOrderApplicationService.updateOrder(request);

    // then
    assertThat(order.getStatus()).isEqualTo(OrderStatus.SHIPPING);
    verify(orderService).findById(orderId);
    verify(orderService).save(order);
  }
}