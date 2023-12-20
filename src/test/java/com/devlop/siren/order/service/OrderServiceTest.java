package com.devlop.siren.order.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.devlop.siren.domain.order.domain.Order;
import com.devlop.siren.domain.order.domain.OrderItem;
import com.devlop.siren.domain.order.domain.OrderStatus;
import com.devlop.siren.domain.order.domain.option.CustomOption;
import com.devlop.siren.domain.order.dto.request.OrderStatusRequest;
import com.devlop.siren.domain.order.dto.response.OrderDetailResponse;
import com.devlop.siren.domain.order.repository.CustomOptionRepository;
import com.devlop.siren.domain.order.repository.OrderItemRepository;
import com.devlop.siren.domain.order.repository.OrderRepository;
import com.devlop.siren.domain.order.service.OrderService;
import com.devlop.siren.domain.stock.entity.Stock;
import com.devlop.siren.domain.stock.repository.StockRepository;
import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.fixture.ItemFixture;
import com.devlop.siren.fixture.OrderFixture;
import com.devlop.siren.fixture.UserFixture;
import com.devlop.siren.global.common.response.ResponseCode.ErrorCode;
import com.devlop.siren.global.exception.GlobalException;
import com.devlop.siren.global.util.UserInformation;
import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
  @InjectMocks private OrderService orderService;
  @Mock private OrderRepository orderRepository;
  @Mock private OrderItemRepository orderItemRepository;
  @Mock private StockRepository stockRepository;
  @Mock private CustomOptionRepository customOptionRepository;

  private static User user;
  private static UserDetailsDto userDto;
  private static Store store;
  private static Stock stock;
  private static List<OrderItem> orderItems;
  private static Order order;
  private static OrderStatusRequest orderStatusRequest;

  @BeforeEach
  private void setUp() throws NoSuchFieldException, IllegalAccessException {
    user = UserFixture.get("test@test.com", "password", "nickname");
    userDto = UserFixture.get(UserRole.STAFF);
    store = OrderFixture.get(LocalTime.of(9, 0), LocalTime.of(21, 0));
    orderItems = OrderFixture.getOrderItem(ItemFixture.get(1L));
    stock = Stock.builder().item(ItemFixture.get(1L)).stock(1).store(store).build();
    order = OrderFixture.getOrder(user, store, orderItems);
    orderStatusRequest = new OrderStatusRequest(1L, OrderStatus.READY);
  }

  @Test
  @DisplayName("주문 생성 성공")
  void createOrder() throws NoSuchFieldException, IllegalAccessException {
    stock.update(100);
    CustomOption customOption = orderItems.get(0).getCustomOption();
    Field field = CustomOption.class.getDeclaredField("dtype");
    field.setAccessible(true);
    field.set(customOption, "Beverage");

    when(customOptionRepository.save(any())).thenReturn(mock(CustomOption.class));
    when(stockRepository.findByStoreAndItem(
            store.getStoreId(), orderItems.get(0).getItem().getItemId()))
        .thenReturn(Optional.of(stock));
    when(orderItemRepository.save(any())).thenReturn(mock(OrderItem.class));
    when(orderRepository.save(any())).thenReturn(order);

    OrderDetailResponse response =
        orderService.create(user, store, orderItems, LocalTime.of(13, 00));
    assertThat(response.getStoreName()).isEqualTo("강남대로신사");
    assertThat(response.getOrderState()).isEqualTo(OrderStatus.INIT);
  }

  @Test
  @DisplayName("매장 운영시간이 아니라 주문 생성 실패")
  void createOrderWithNotOperatingTime() {
    store.setOpenTime(LocalTime.of(9, 0));
    store.setCloseTime(LocalTime.of(21, 0));

    GlobalException e =
        assertThrows(
            GlobalException.class,
            () -> {
              orderService.create(user, store, orderItems, LocalTime.of(22, 00));
            });

    assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NOT_OPERATING_TIME);
  }

  @Test
  @DisplayName("재고 부족으로 주문 생성 실패")
  void createOrderWithInsufficientStock() {
    // in stock = 1, order quantity 3;
    when(customOptionRepository.save(any())).thenReturn(mock(CustomOption.class));
    when(stockRepository.findByStoreAndItem(
            store.getStoreId(), orderItems.get(0).getItem().getItemId()))
        .thenThrow(new GlobalException(ErrorCode.OUT_OF_STOCK));

    GlobalException e =
        assertThrows(
            GlobalException.class,
            () -> orderService.create(user, store, orderItems, LocalTime.of(13, 00)));
    assertThat(e.getErrorCode()).isEqualTo(ErrorCode.OUT_OF_STOCK);
  }

  @Test
  @DisplayName("주문 취소 요청")
  void cancelOrder() throws NoSuchFieldException, IllegalAccessException {
    Field orderId = Order.class.getDeclaredField("id");
    orderId.setAccessible(true);
    orderId.set(order, 1L);

    CustomOption customOption = orderItems.get(0).getCustomOption();
    Field dtype = CustomOption.class.getDeclaredField("dtype");
    dtype.setAccessible(true);
    dtype.set(customOption, "Beverage");

    try (MockedStatic<UserInformation> userInformation = mockStatic(UserInformation.class)) {
      userInformation.when(() -> UserInformation.validStaffOrAdmin(userDto)).thenReturn(true);
      when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
      when(stockRepository.findByStoreAndItem(
              store.getStoreId(), orderItems.get(0).getItem().getItemId()))
          .thenReturn(Optional.of(mock(Stock.class)));

      OrderDetailResponse response = orderService.cancel(order.getId(), userDto);
      assertThat(response.getOrderState()).isEqualTo(OrderStatus.CANCELED);
    }
  }

  @Test
  @DisplayName("주문 아이디가 존재하지 않아 주문 취소를 할 수 없다")
  void notCancelOrderWithNotFoundedOrderId() throws NoSuchFieldException, IllegalAccessException {
    Field field = Order.class.getDeclaredField("id");
    field.setAccessible(true);
    field.set(order, 1L);
    try (MockedStatic<UserInformation> userInformation = mockStatic(UserInformation.class)) {
      when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

      GlobalException e =
          assertThrows(GlobalException.class, () -> orderService.cancel(order.getId(), userDto));
      assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_ORDER);
    }
  }

  @Test
  @DisplayName("이미 진행중인 주문이어서 주문 취소를 할 수 없다")
  void notCanceledOrderWithAlreadyOrdered() throws NoSuchFieldException, IllegalAccessException {
    Field field = Order.class.getDeclaredField("id");
    field.setAccessible(true);
    field.set(order, 1L);
    order.setStatus(OrderStatus.READY);
    try (MockedStatic<UserInformation> userInformation = mockStatic(UserInformation.class)) {
      when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

      GlobalException e =
          assertThrows(GlobalException.class, () -> orderService.cancel(order.getId(), userDto));
      assertThat(e.getErrorCode()).isEqualTo(ErrorCode.ALREADY_ORDERED);
    }
  }

  @Test
  @DisplayName("주문 상태 변경")
  void updateOrderStatue() throws NoSuchFieldException, IllegalAccessException {
    CustomOption customOption = orderItems.get(0).getCustomOption();
    Field field = CustomOption.class.getDeclaredField("dtype");
    field.setAccessible(true);
    field.set(customOption, "Beverage");

    try (MockedStatic<UserInformation> userInformation = mockStatic(UserInformation.class)) {
      userInformation.when(() -> UserInformation.validStaffOrAdmin(userDto)).thenReturn(true);
      when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
      OrderDetailResponse response = orderService.updateStatus(orderStatusRequest, userDto);
      assertThat(response.getOrderState()).isEqualTo(OrderStatus.READY);
    }
  }

  @Test
  @DisplayName("이미 완료된 주문이라 주문상태를 변경할 수 없다")
  void updateOrderStatusWithAlreadyCompletedOrder() {
    order.setStatus(OrderStatus.COMPLETED);

    try (MockedStatic<UserInformation> userInformation = mockStatic(UserInformation.class)) {
      userInformation.when(() -> UserInformation.validStaffOrAdmin(userDto)).thenReturn(true);
      when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
      GlobalException exception =
          assertThrows(
              GlobalException.class, () -> orderService.updateStatus(orderStatusRequest, userDto));
      assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.ALREADY_COMPLETED_ORDER);
    }
  }
}
