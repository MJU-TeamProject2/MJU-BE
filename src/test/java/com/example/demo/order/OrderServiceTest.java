package com.example.demo.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.demo.clothes.entity.ClothesCategory;
import com.example.demo.clothes.entity.GenderCategory;
import com.example.demo.customer.entity.BodyType;
import com.example.demo.customer.entity.Gender;
import com.example.demo.order.service.OrderService;
import com.example.demo.util.TestResultLogger;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.cart.entity.Cart;
import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.entity.ClothesSize;
import com.example.demo.customer.entity.Address;
import com.example.demo.customer.entity.Customer;
import com.example.demo.customer.entity.Payment;
import com.example.demo.exception.ClothesInsufficientStockException;
import com.example.demo.order.entity.Order;
import com.example.demo.order.entity.OrderStatus;
import com.example.demo.order.exception.OrderNotFoundException;
import com.example.demo.order.repository.OrderRepository;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, TestResultLogger.class})
@Slf4j
class OrderServiceTest {

  @InjectMocks
  private OrderService orderService;

  @Mock
  private OrderRepository orderRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    orderService = new OrderService(orderRepository);
  }

  @Test
  void 고객의_주문_목록을_조회한다() {
    // given
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Order order = Order.builder()
        .id(1L)
        .customer(customer)
        .status(OrderStatus.PREPARING)
        .build();
    given(orderRepository.findByCustomer(customer)).willReturn(List.of(order));

    // when
    List<Order> result = orderService.findByCustomer(customer);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getId()).isEqualTo(1L);
  }

  @Test
  void 단일_주문을_조회한다() {
    // given
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Order order = Order.builder()
        .id(1L)
        .customer(customer)
        .status(OrderStatus.PREPARING)
        .build();
    given(orderRepository.findOneByIdAndCustomer(1L, customer)).willReturn(Optional.of(order));

    // when
    Order result = orderService.findOneByIdAndCustomer(1L, customer);

    // then
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getCustomer()).isEqualTo(customer);
  }

  @Test
  void 존재하지_않는_주문을_조회할_수_없다() {
    // given
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    given(orderRepository.findOneByIdAndCustomer(1L, customer)).willReturn(Optional.empty());

    // when & then
    assertThrows(OrderNotFoundException.class,
        () -> orderService.findOneByIdAndCustomer(1L, customer));
  }

  @Test
  void 새로운_주문을_생성한다() {
    // given
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

    // when
    orderService.addToOrderItem(customer, cart, address, payment);

    // then
    verify(orderRepository).save(any(Order.class));
  }

  @Test
  void 재고보다_많은_수량을_주문할_수_없다() {
    // given
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    ClothesSize clothesSize = ClothesSize.builder().quantity(5).build();
    Cart cart = Cart.builder()
        .clothesSize(clothesSize)
        .quantity(10)
        .build();
    Address address = Address.builder()
        .id(1L)
        .customer(customer)
        .build();
    Payment payment = Payment.builder()
        .id(1L)
        .customer(customer)
        .build();

    // when & then
    assertThrows(ClothesInsufficientStockException.class,
        () -> orderService.addToOrderItem(customer, cart, address, payment));
  }

  @Test
  void 주문을_삭제한다() {
    // given
    Order order = Order.builder().id(1L).build();

    // when
    orderService.deleteFromOrder(order);

    // then
    verify(orderRepository).delete(order);
  }
}

