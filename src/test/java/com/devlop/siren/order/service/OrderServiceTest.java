package com.devlop.siren.order.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.devlop.siren.domain.order.domain.Order;
import com.devlop.siren.domain.order.domain.OrderItem;
import com.devlop.siren.domain.order.domain.OrderStatus;
import com.devlop.siren.domain.order.domain.option.CustomOption;
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
import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Slf4j
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

  @BeforeEach
  private void setUp() {
    user = UserFixture.get("test@test.com", "password", "nickname");
    userDto = UserFixture.get(UserRole.CUSTOMER);
    store = OrderFixture.get(LocalTime.of(9, 0), LocalTime.of(21, 0));
    orderItems = OrderFixture.getOrderItem(ItemFixture.get());
    stock = Stock.builder().item(ItemFixture.get()).stock(1).store(store).build();
    order = OrderFixture.getOrder(user, store, orderItems);
  }

  @Test
  @DisplayName("주문 생성 성공")
  void createOrder() {
    stock.update(100);

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
        .thenThrow(new GlobalException(ErrorCode.ORDER_QUANTITY_IN_STOCK));

    GlobalException e =
        assertThrows(
            GlobalException.class,
            () -> orderService.create(user, store, orderItems, LocalTime.of(13, 00)));
    assertThat(e.getErrorCode()).isEqualTo(ErrorCode.ORDER_QUANTITY_IN_STOCK);
  }

  @Test
  @DisplayName("주문 취소 요청")
  void cancelOrder() throws NoSuchFieldException, IllegalAccessException {
    Field field = Order.class.getDeclaredField("id");
    field.setAccessible(true);
    field.set(order, 1L);

    when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
    when(stockRepository.findByStoreAndItem(
            store.getStoreId(), orderItems.get(0).getItem().getItemId()))
        .thenReturn(Optional.of(mock(Stock.class)));

    OrderDetailResponse response = orderService.cancel(order.getId(), userDto);
    assertThat(response.getOrderState()).isEqualTo(OrderStatus.CANCELLED);
  }

  @Test
  @DisplayName("주문 아이디가 존재하지 않아 주문 취소를 할 수 없다")
  void notCancelOrderWithNotFoundedOrderId() throws NoSuchFieldException, IllegalAccessException {
    Field field = Order.class.getDeclaredField("id");
    field.setAccessible(true);
    field.set(order, 1L);

    when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

    GlobalException e =
        assertThrows(GlobalException.class, () -> orderService.cancel(order.getId(), userDto));
    assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_ORDER);
  }

  @Test
  @DisplayName("이미 진행중인 주문이어서 주문 취소를 할 수 없다")
  void notCanceledOrderWithAlreadyOrdered() throws NoSuchFieldException, IllegalAccessException {
    Field field = Order.class.getDeclaredField("id");
    field.setAccessible(true);
    field.set(order, 1L);
    order.setStatus(OrderStatus.READY);

    when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

    GlobalException e =
        assertThrows(GlobalException.class, () -> orderService.cancel(order.getId(), userDto));
    assertThat(e.getErrorCode()).isEqualTo(ErrorCode.ALREADY_ORDERED);
  }
}
