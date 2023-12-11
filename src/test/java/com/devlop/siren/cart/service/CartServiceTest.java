package com.devlop.siren.cart.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.devlop.siren.domain.cart.service.CartService;
import com.devlop.siren.domain.item.entity.AllergyType;
import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.entity.option.OptionDetails;
import com.devlop.siren.domain.item.entity.option.OptionTypeGroup;
import com.devlop.siren.domain.item.entity.option.SizeType;
import com.devlop.siren.domain.item.repository.ItemRepository;
import com.devlop.siren.domain.order.dto.request.CustomOptionRequest;
import com.devlop.siren.domain.order.dto.request.OrderItemRequest;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.domain.user.repository.UserRepository;
import com.devlop.siren.fixture.ItemFixture;
import com.devlop.siren.fixture.UserFixture;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
  @InjectMocks private CartService cartService;
  @Mock private ItemRepository itemRepository;
  @Mock private UserRepository userRepository;
  @Mock private RedisTemplate<String, OrderItemRequest> cartRedisTemplate;

  private final OrderItemRequest validOrderItemRequest =
      OrderItemRequest.builder()
          .itemId(1L)
          .warm(false)
          .takeout(false)
          .quantity(5)
          .customOption(
              CustomOptionRequest.builder()
                  .cupSize(SizeType.TALL)
                  .drizzles(
                      Set.of(
                          new OptionDetails.DrizzleDetail(OptionTypeGroup.DrizzleType.CARAMEL, 2)))
                  .espresso(
                      new OptionDetails.EspressoDetail(OptionTypeGroup.EspressoType.ORIGINAL, 2))
                  .milk(OptionTypeGroup.MilkType.ORIGINAL)
                  .build())
          .build();
  private final OrderItemRequest invalidOrderItemRequest = OrderItemRequest.builder().build();

  private final UserDetailsDto userDetailsDto = UserFixture.get(UserRole.CUSTOMER);
  private final User user =
      UserFixture.get(userDetailsDto.getEmail(), userDetailsDto.getPassword(), "TEST");
  private final Item item = ItemFixture.get(EnumSet.of(AllergyType.SOYBEAN));
  private final ListOperations<String, OrderItemRequest> listOperations =
      Mockito.mock(ListOperations.class);
  private String cartKey;

  @BeforeEach
  public void setUp() {
    ReflectionTestUtils.setField(user, "id", 1L);
    ReflectionTestUtils.setField(item, "itemId", 1L);
    ReflectionTestUtils.setField(cartService, "listOperations", listOperations);
    cartKey = "Cart - UserId" + ":" + user.getId();
  }

  @Test
  @DisplayName("장바구니에 상품을 추가할 수 있다.")
  void add() {
    when(itemRepository.findById(validOrderItemRequest.getItemId())).thenReturn(Optional.of(item));
    assertThat(
            cartService
                .add(validOrderItemRequest, userDetailsDto)
                .getOrderItemRequestList()
                .contains(validOrderItemRequest))
        .isTrue();
  }

  @Test
  @DisplayName("장바구니에 동일 상품이 있을시 수량을 증가한다.")
  void addToExist() {
    when(itemRepository.findById(validOrderItemRequest.getItemId())).thenReturn(Optional.of(item));
    when(listOperations.range(cartKey, 0, listOperations.size(cartKey) - 1))
        .thenReturn(List.of(validOrderItemRequest));
    assertThat(
            cartService
                .add(validOrderItemRequest, userDetailsDto)
                .getOrderItemRequestList()
                .get(0)
                .getQuantity())
        .isEqualTo(10);
  }

  @Test
  @DisplayName("존재하지 않는 아이템을 장바구니에 담을 시 예외가 발생한다.")
  void failAddInvalidItem() {
    assertThatThrownBy(() -> cartService.add(validOrderItemRequest, userDetailsDto))
        .isInstanceOf(GlobalException.class)
        .hasMessageContaining(ResponseCode.ErrorCode.NOT_FOUND_ITEM.getMESSAGE());
  }

  @Test
  @DisplayName("사용자의 알러지를 유발할 수 있는 아이템을 장바구니에 담을 시 예외가 발생한다.")
  void failAddCauseAllergy() {
    Item allergyItem = ItemFixture.get(EnumSet.of(AllergyType.MILK));
    when(itemRepository.findById(validOrderItemRequest.getItemId()))
        .thenReturn(Optional.of(allergyItem));
    assertThatThrownBy(() -> cartService.add(validOrderItemRequest, userDetailsDto))
        .isInstanceOf(GlobalException.class)
        .hasMessageContaining(ResponseCode.ErrorCode.CAUSE_ALLERGY_IN_CART.getMESSAGE());
  }

  @Test
  @DisplayName("장바구니 상품을 조회할 수 있다.")
  void retrieve() {
    when(listOperations.range(cartKey, 0, listOperations.size(cartKey) - 1))
        .thenReturn(List.of(validOrderItemRequest));
    assertThat(
            cartService
                .retrieve(userDetailsDto)
                .getOrderItemRequestList()
                .contains(validOrderItemRequest))
        .isTrue();
  }

  @Test
  @DisplayName("장바구니 상품을 전체 삭제할 수 있다.")
  void removeAll() {
    cartService.removeAll(userDetailsDto);
    verify(listOperations).trim(cartKey, 1, 0);
  }

  @Test
  @DisplayName("장바구니의 특정 상품을 삭제할 수 있다.")
  void remove() {
    when(listOperations.range(cartKey, 0, listOperations.size(cartKey) - 1))
        .thenReturn(List.of(validOrderItemRequest));
    cartService.remove(validOrderItemRequest, userDetailsDto);
    verify(listOperations).remove(cartKey, 1, validOrderItemRequest);
  }

  @Test
  @DisplayName("장바구니에 존재하지 않는 상품을 삭제할 경우 예외가 발생한다.")
  void failRemove() {
    assertThatThrownBy(() -> cartService.remove(validOrderItemRequest, userDetailsDto))
        .isInstanceOf(GlobalException.class)
        .hasMessageContaining(ResponseCode.ErrorCode.NOT_FOUND_ITEM.getMESSAGE());
  }

  @Test
  @DisplayName("장바구니의 특정 상품을 수정할 수 있다.")
  void update() {
    when(listOperations.range(cartKey, 0, listOperations.size(cartKey) - 1))
        .thenReturn(List.of(validOrderItemRequest));
    cartService.update(validOrderItemRequest, userDetailsDto);
    verify(listOperations).set(cartKey, 0, validOrderItemRequest);
  }

  @Test
  @DisplayName("장바구니에 존재하지 않는 상품을 수정할 경우 예외가 발생한다.")
  void failUpdate() {
    assertThatThrownBy(() -> cartService.update(validOrderItemRequest, userDetailsDto))
        .isInstanceOf(GlobalException.class)
        .hasMessageContaining(ResponseCode.ErrorCode.NOT_FOUND_ITEM.getMESSAGE());
  }
}
