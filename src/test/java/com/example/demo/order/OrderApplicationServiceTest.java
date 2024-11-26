package com.example.demo.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.demo.clothes.entity.ClothesCategory;
import com.example.demo.clothes.entity.GenderCategory;
import com.example.demo.customer.entity.BodyType;
import com.example.demo.customer.entity.Gender;
import com.example.demo.order.dto.response.GetOrderDetailResponse;
import com.example.demo.order.dto.response.GetOrderResponse;
import com.example.demo.order.entity.OrderStatus;
import com.example.demo.order.service.application.OrderApplicationService;
import com.example.demo.util.TestResultLogger;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.cart.entity.Cart;
import com.example.demo.cart.service.CartService;
import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.entity.ClothesSize;
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
import com.example.demo.order.entity.Order;
import com.example.demo.order.service.OrderService;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, TestResultLogger.class})
@Slf4j
class OrderApplicationServiceTest {

  @InjectMocks
  private OrderApplicationService orderApplicationService;

  @Mock private OrderService orderService;
  @Mock private CustomerService customerService;
  @Mock private CartService cartService;
  @Mock private AddressService addressService;
  @Mock private PaymentService paymentService;
  @Mock private ClothesSizeService clothesSizeService;
  @Mock private S3Service s3Service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    orderApplicationService = new OrderApplicationService(
        orderService,
        customerService,
        cartService,
        addressService,
        paymentService,
        clothesSizeService,
        s3Service
    );
  }

  @Test
  void 고객의_주문_목록을_조회한다() {
    // given
    Long customerId = 1L;
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Clothes clothes = Clothes.builder()
        .imageUrl("image.jpg")
        .detailUrl("detail.jpg")
        .build();
    ClothesSize clothesSize = ClothesSize.builder().quantity(10).build();
    Order order = Order.builder()
        .clothes(clothes)
        .clothesSize(clothesSize)
        .build();

    given(customerService.findById(customerId)).willReturn(customer);
    given(orderService.findByCustomer(customer)).willReturn(List.of(order));
    given(s3Service.generatePresignedUrl(any())).willReturn("presigned-url");

    // when
    List<GetOrderResponse> result = orderApplicationService.getOrderItems(customerId);

    // then
    assertThat(result).hasSize(1);
    verify(customerService).findById(customerId);
    verify(orderService).findByCustomer(customer);
  }

  @Test
  void 주문_상세정보를_조회한다() {
    // given
    Long customerId = 1L;
    Long orderId = 1L;
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Clothes clothes = Clothes.builder()
        .imageUrl("image.jpg")
        .detailUrl("detail.jpg")
        .build();
    ClothesSize clothesSize = ClothesSize.builder().quantity(10).build();
    Order order = Order.builder()
        .clothes(clothes)
        .clothesSize(clothesSize)
        .payment(Payment.builder().id(1L).build())
        .address(Address.builder().id(1L).build())
        .quantity(10)
        .build();

    given(customerService.findById(customerId)).willReturn(customer);
    given(orderService.findOneByIdAndCustomer(orderId, customer)).willReturn(order);
    given(s3Service.generatePresignedUrl(any())).willReturn("presigned-url");

    // when
    GetOrderDetailResponse result = orderApplicationService.getOrderItemDetail(customerId, orderId);

    // then
    assertThat(result).isNotNull();
  }

  @Test
  void 장바구니에서_주문을_생성한다() {
    // given
    Long customerId = 1L;
    Long cartId = 1L;
    Long addressId = 1L;
    Long paymentId = 1L;

    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Clothes clothes = new Clothes(1L, ClothesCategory.DRESSES, "dummy", "image", 10000, GenderCategory.FEMALE, "1234", 0, "detail", " objectKey", "objectFemaleKey", "mtlKey");
    ClothesSize clothesSize = ClothesSize.builder().quantity(10).build();
    Cart cart = Cart.builder()
        .clothes(clothes)
        .clothesSize(clothesSize)
        .quantity(5)
        .build();
    Address address = Address.builder()
        .id(1L)
        .customer(customer)
        .build();
    Payment payment = Payment.builder()
        .id(1L)
        .customer(customer)
        .build();

    AddToOrderItemRequest request = new AddToOrderItemRequest(cartId, addressId, paymentId);

    given(customerService.findById(customerId)).willReturn(customer);
    given(cartService.findByIdAndCustomerId(cartId, customerId)).willReturn(cart);
    given(addressService.findOneByIdAndCustomer(addressId, customer)).willReturn(address);
    given(paymentService.findOneByIdAndCustomer(paymentId, customer)).willReturn(payment);

    // when
    orderApplicationService.addToOrderItem(customerId, request);

    // then
    verify(orderService).addToOrderItem(customer, cart, address, payment);
    verify(cartService).deleteFromCart(cart);
  }

  @Test
  void 주문정보를_수정한다() {
    // given
    Long customerId = 1L;
    Long orderId = 1L;
    Long addressId = 1L;

    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Order order = Order.builder().status(OrderStatus.PREPARING).build();
    Address address = Address.builder()
        .id(1L)
        .customer(customer)
        .build();

    UpdateOrderItemRequest request = new UpdateOrderItemRequest(orderId, addressId);

    given(customerService.findById(customerId)).willReturn(customer);
    given(orderService.findOneByIdAndCustomer(orderId, customer)).willReturn(order);
    given(addressService.findOneByIdAndCustomer(addressId, customer)).willReturn(address);

    // when
    orderApplicationService.updateOrderItem(customerId, request);

    // then
    verify(orderService).save(order);
  }

  @Test
  void 주문을_삭제한다() {
    // given
    Long customerId = 1L;
    Long orderId = 1L;
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    ClothesSize clothesSize = ClothesSize.builder().quantity(10).build();
    Order order = Order.builder()
        .clothesSize(clothesSize)
        .quantity(10)
        .build();

    given(customerService.findById(customerId)).willReturn(customer);
    given(orderService.findOneByIdAndCustomer(orderId, customer)).willReturn(order);

    // when
    orderApplicationService.deleteOrderItem(customerId, orderId);

    // then
    verify(orderService).deleteFromOrder(order);
    verify(clothesSizeService).updateClothesQuantity(any(), any());
  }
}
