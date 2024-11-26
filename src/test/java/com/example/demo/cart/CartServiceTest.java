package com.example.demo.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.demo.cart.service.CartService;
import com.example.demo.clothes.entity.ClothesCategory;
import com.example.demo.clothes.entity.GenderCategory;
import com.example.demo.customer.entity.BodyType;
import com.example.demo.customer.entity.Gender;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.cart.entity.Cart;
import com.example.demo.cart.repository.CartRepository;
import com.example.demo.clothes.entity.Clothes;
import com.example.demo.clothes.entity.ClothesSize;
import com.example.demo.customer.entity.Customer;
import com.example.demo.cart.exception.CartNotFoundException;
import com.example.demo.exception.ClothesInsufficientStockException;

class CartServiceTest {

  private CartService cartService;

  @Mock
  private CartRepository cartRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    cartService = new CartService(cartRepository);
  }

  @Test
  void 장바구니에서_고객의_상품목록을_조회한다() {
    // given
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Cart cart = Cart.builder()
        .id(1L)
        .customer(customer)
        .clothes(new Clothes(1L, ClothesCategory.DRESSES, "dummy", "image", 10000, GenderCategory.FEMALE, "1234", 0,  "detail", " objectKey", "objectFemaleKey", "mtlKey"))
        .quantity(1)
        .build();
    given(cartRepository.findByCustomer(customer)).willReturn(List.of(cart));

    // when
    List<Cart> result = cartService.findByCustomer(customer);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getId()).isEqualTo(1L);
  }

  @Test
  void 장바구니에_새로운_상품을_추가한다() {
    // given
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Clothes clothes = new Clothes(1L, ClothesCategory.DRESSES, "dummy", "image", 10000, GenderCategory.FEMALE, "1234", 0,  "detail", " objectKey", "objectFemaleKey", "mtlKey");
    ClothesSize clothesSize = ClothesSize.builder().quantity(10).build();
    given(cartRepository.findByCustomerAndClothesAndClothesSize(customer, clothes, clothesSize))
        .willReturn(Optional.empty());

    // when
    cartService.addToCart(customer, clothes, 5, clothesSize);

    // then
    verify(cartRepository).save(any(Cart.class));
  }

  @Test
  void 재고보다_많은_수량을_장바구니에_추가할_수_없다() {
    // given
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Clothes clothes = new Clothes(1L, ClothesCategory.DRESSES, "dummy", "image", 10000, GenderCategory.FEMALE, "1234", 0,  "detail", " objectKey", "objectFemaleKey", "mtlKey");
    ClothesSize clothesSize = ClothesSize.builder().quantity(5).build();
    given(cartRepository.findByCustomerAndClothesAndClothesSize(customer, clothes, clothesSize))
        .willReturn(Optional.empty());

    // when & then
    assertThrows(ClothesInsufficientStockException.class,
        () -> cartService.addToCart(customer, clothes, 10, clothesSize));
  }

  @Test
  void 장바구니_상품의_수량을_업데이트한다() {
    // given
    ClothesSize clothesSize = ClothesSize.builder().quantity(10).build();
    Cart cart = Cart.builder()
        .id(1L)
        .quantity(1)
        .clothesSize(clothesSize)
        .build();

    // when
    cartService.updateCartItem(cart, 5);

    // then
    assertThat(cart.getQuantity()).isEqualTo(5);
    verify(cartRepository).save(cart);
  }

  @Test
  void 장바구니에서_상품을_삭제한다() {
    // given
    Cart cart = Cart.builder().id(1L).build();

    // when
    cartService.deleteFromCart(cart);

    // then
    verify(cartRepository).delete(cart);
  }

  @Test
  void 존재하지_않는_장바구니_상품을_조회할_수_없다() {
    // given
    Long cartId = 1L;
    Long customerId = 1L;
    given(cartRepository.findByIdAndCustomerId(cartId, customerId))
        .willReturn(Optional.empty());

    // when & then
    assertThrows(CartNotFoundException.class,
        () -> cartService.findByIdAndCustomerId(cartId, customerId));
  }
}