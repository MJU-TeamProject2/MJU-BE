package com.example.demo.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.demo.cart.service.application.CartApplicationService;
import com.example.demo.clothes.entity.ClothesCategory;
import com.example.demo.clothes.entity.GenderCategory;
import com.example.demo.clothes.entity.Size;
import com.example.demo.customer.entity.BodyType;
import com.example.demo.customer.entity.Gender;
import com.example.demo.util.TestResultLogger;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, TestResultLogger.class})
@Slf4j
class CartApplicationServiceTest {

  @InjectMocks
  private CartApplicationService cartApplicationService;

  @Mock
  private CartService cartService;
  @Mock
  private CustomerService customerService;
  @Mock
  private ClothesService clothesService;
  @Mock
  private ClothesSizeService clothesSizeService;
  @Mock
  private S3Service s3Service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    cartApplicationService = new CartApplicationService(
        cartService,
        customerService,
        clothesService,
        clothesSizeService,
        s3Service
    );
  }

  @Test
  void 고객의_장바구니_목록을_조회한다() {
    // given
    Long customerId = 1L;
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Clothes clothes = new Clothes(1L, ClothesCategory.DRESSES, "dummy", "image", 10000, GenderCategory.FEMALE, "1234", 0,  "detail", " objectKey", "objectFemaleKey", "mtlKey");
    Cart cart = Cart.builder()
        .clothesSize(new ClothesSize(1L, clothes, Size.M, 10))
        .clothes(clothes)
        .build();

    given(customerService.findById(customerId)).willReturn(customer);
    given(cartService.findByCustomer(customer)).willReturn(List.of(cart));
    given(s3Service.generatePresignedUrl(any())).willReturn("presigned-url");

    // when
    List<GetCartResponse> result = cartApplicationService.getCartItems(customerId);

    // then
    assertThat(result).hasSize(1);
    verify(customerService).findById(customerId);
    verify(cartService).findByCustomer(customer);
  }

  @Test
  void 장바구니에_상품을_추가한다() {
    // given
    Long customerId = 1L;
    Long clothesId = 1L;
    Customer customer = new Customer("test", "test", 1, Gender.F, "test", "test", "010-3333-3333", 123, 123, BodyType.LARGE_TRIANGLE);
    Clothes clothes = Clothes.builder().id(clothesId).build();
    ClothesSize clothesSize = ClothesSize.builder().size(Size.M).build();
    AddToCartItemRequest request = new AddToCartItemRequest(clothesId, 1, Size.M);

    given(customerService.findById(customerId)).willReturn(customer);
    given(clothesService.findById(clothesId)).willReturn(clothes);
    given(clothesSizeService.findByClothesIdAndSize(clothes, Size.M)).willReturn(clothesSize);

    // when
    cartApplicationService.addToCart(customerId, request);

    // then
    verify(cartService).addToCart(customer, clothes, request.quantity(), clothesSize);
  }

  @Test
  void 장바구니_상품의_수량을_수정한다() {
    // given
    Long customerId = 1L;
    Long cartId = 1L;
    Cart cart = Cart.builder().id(cartId).build();
    UpdateCartItemRequest request = new UpdateCartItemRequest(cartId, 2);

    given(cartService.findByIdAndCustomerId(cartId, customerId)).willReturn(cart);

    // when
    cartApplicationService.updateCartItem(customerId, request);

    // then
    verify(cartService).updateCartItem(cart, request.quantity());
  }

  @Test
  void 장바구니에서_상품을_삭제한다() {
    // given
    Long customerId = 1L;
    Long cartId = 1L;
    Cart cart = Cart.builder().id(cartId).build();

    given(cartService.findByIdAndCustomerId(cartId, customerId)).willReturn(cart);

    // when
    cartApplicationService.deleteFromCart(customerId, cartId);

    // then
    verify(cartService).deleteFromCart(cart);
  }
}
