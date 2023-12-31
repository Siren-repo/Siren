package com.devlop.siren.order.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devlop.siren.domain.order.controller.OrderController;
import com.devlop.siren.domain.order.domain.OrderStatus;
import com.devlop.siren.domain.order.dto.request.OrderCreateRequest;
import com.devlop.siren.domain.order.dto.request.OrderItemRequest;
import com.devlop.siren.domain.order.dto.request.OrderStatusRequest;
import com.devlop.siren.domain.order.service.OrderService;
import com.devlop.siren.domain.order.service.OrderUseCase;
import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.fixture.OrderFixture;
import com.devlop.siren.fixture.UserFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private OrderService orderService;

  @MockBean private OrderUseCase orderUseCase;

  private List<OrderItemRequest> orderItemRequest;
  private Store store;
  private OrderCreateRequest validOrderCreateRequest;

  @BeforeEach
  void init() throws NoSuchFieldException, IllegalAccessException {
    store = OrderFixture.get(LocalTime.of(9, 00), LocalTime.of(18, 00));
    orderItemRequest = OrderFixture.getOrderItemRequest();
    validOrderCreateRequest = OrderFixture.get(store.getStoreId(), orderItemRequest);
    UserDetailsDto dto = UserFixture.get(UserRole.ADMIN);

    SecurityContextHolder.getContext()
        .setAuthentication(
            new UsernamePasswordAuthenticationToken(dto, null, dto.getAuthorities()));
  }

  @Test
  @DisplayName("주문 생성")
  void createOrder() throws Exception {
    mvc.perform(
            post("/api/orders")
                .with(csrf())
                .content(objectMapper.writeValueAsString(validOrderCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("주문 생성 시 필수값이 없어 예외가 발생한다")
  void createOrderWithNoItemOption() throws Exception {
    OrderCreateRequest invalidRequest =
        OrderFixture.get(store.getStoreId(), OrderFixture.invalidOrderItemRequest());

    mvc.perform(
            post("/api/orders")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @Test
  @DisplayName("주문 취소")
  void cancelOrder() throws Exception {
    Long orderId = 1L;
    mvc.perform(put("/api/orders/{orderId}/cancel", orderId).with(csrf()))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("권한이 없어 주문 취소를 할 수 없다")
  void cancelOrderWithNotAuth() throws Exception {
    Long orderId = 1L;
    UserDetailsDto dto = UserFixture.get(UserRole.CUSTOMER);
    SecurityContextHolder.getContext()
        .setAuthentication(
            new UsernamePasswordAuthenticationToken(dto, null, dto.getAuthorities()));

    mvc.perform(put("/api/orders/{orderId}/cancel", orderId).with(csrf()))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @DisplayName("주문 상태 변경")
  void updateOrderStatus() throws Exception {
    OrderStatusRequest request = new OrderStatusRequest(1L, OrderStatus.READY);
    mvc.perform(
            put("/api/orders/status")
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("권한이 없어 주문상태 변경을 할 수 없다")
  void updateOrderStatusWithNotAuth() throws Exception {
    OrderStatusRequest request = new OrderStatusRequest(1L, OrderStatus.READY);
    UserDetailsDto dto = UserFixture.get(UserRole.CUSTOMER);
    SecurityContextHolder.getContext()
        .setAuthentication(
            new UsernamePasswordAuthenticationToken(dto, null, dto.getAuthorities()));
    mvc.perform(
            put("/api/orders/status")
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @DisplayName("주문 상태 변경 시 필수값 검증에서 실패한다")
  void updateOrderStatusWithEmptyOrderId() throws Exception {
    OrderStatusRequest request = new OrderStatusRequest(null, OrderStatus.READY);
    mvc.perform(
            put("/api/orders/status")
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }
}
