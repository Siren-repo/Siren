package com.devlop.siren.order.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devlop.siren.domain.order.controller.OrderController;
import com.devlop.siren.domain.order.dto.request.OrderCreateRequest;
import com.devlop.siren.domain.order.dto.request.OrderItemRequest;
import com.devlop.siren.domain.order.service.OrderService;
import com.devlop.siren.domain.order.service.OrderUseCase;
import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.fixture.ItemFixture;
import com.devlop.siren.fixture.OrderFixture;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

  @Autowired MockMvc mvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean OrderService orderService;

  @MockBean OrderUseCase orderUseCase;

  private List<OrderItemRequest> orderItemRequest;
  private Store store;
  private OrderCreateRequest validOrderCreateRequest;

  @BeforeEach
  void init() {
    store = OrderFixture.get(LocalTime.of(9, 00), LocalTime.of(18, 00));
    orderItemRequest = OrderFixture.getOrderItemRequest(ItemFixture.get());
    validOrderCreateRequest = OrderFixture.get(store.getStoreId(), orderItemRequest);
  }

  @Test
  @DisplayName("주문 생성")
  @WithMockUser
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
  @WithMockUser
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
  @WithMockUser
  void cancelOrder() throws Exception {
    Long orderId = 1L;
    mvc.perform(put("/api/orders/{orderId}", orderId).with(csrf()))
        .andExpect(status().isOk())
        .andDo(print());
  }
}
