package com.devlop.siren.cart.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devlop.siren.domain.cart.controller.CartController;
import com.devlop.siren.domain.cart.service.CartService;
import com.devlop.siren.domain.item.entity.option.OptionDetails;
import com.devlop.siren.domain.item.entity.option.OptionTypeGroup;
import com.devlop.siren.domain.item.entity.option.SizeType;
import com.devlop.siren.domain.order.dto.request.CustomOptionRequest;
import com.devlop.siren.domain.order.dto.request.OrderItemRequest;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.fixture.OrderFixture;
import com.devlop.siren.fixture.UserFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CartController.class)
class CartControllerTest {
  @Autowired MockMvc mvc;

  @MockBean CartService cartService;

  @Autowired private ObjectMapper objectMapper;
  private OrderItemRequest validOrderItemRequest;

  private OrderItemRequest invalidOrderItemRequest;
  private UserDetailsDto userDetailsDto;


  @BeforeEach
  void setUp(){
    validOrderItemRequest = OrderItemRequest.builder()
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
    invalidOrderItemRequest = OrderItemRequest.builder().build();
    userDetailsDto = UserFixture.get(UserRole.CUSTOMER);
    SecurityContextHolder.getContext()
            .setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            userDetailsDto, null, userDetailsDto.getAuthorities()));
  }
  @Test
  @DisplayName("valid 조건에 맞는 파라미터를 넘기면 장바구니 생성에 성공한다 - DTO 검증")
  void add() throws Exception {
    mvc.perform(
            post("/api/cart")
                .with(csrf())
                .content(objectMapper.writeValueAsString(validOrderItemRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("invalid 조건에 맞는 파라미터를 넘기면 장바구니 생성에 실패한다 - DTO 검증")
  void failAdd() throws Exception {
    mvc.perform(
            post("/api/cart")
                .with(csrf())
                .content(objectMapper.writeValueAsString(invalidOrderItemRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }


  @Test
  @DisplayName("장바구니 조회에 성공한다 - DTO 검증")
  void find() throws Exception {
    mvc.perform(
            get("/api/cart")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("장바구니 안의 모든 요소 삭제에 성공한다 - DTO 검증")
  void removeAll() throws Exception {
    mvc.perform(
            delete("/api/cart/all")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("valid 조건에 맞는 파라미터를 넘기면 장바구니 안의 특정 아이템을 삭제에 성공한다. - DTO 검증")
  void remove() throws Exception {
    mvc.perform(
            put("/api/cart/remove")
                .with(csrf())
                .content(objectMapper.writeValueAsString(validOrderItemRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("invalid 조건에 맞는 파라미터를 넘기면 장바구니 안의 특정 아이템을 삭제에 실패한다. - DTO 검증")
  void failRemove() throws Exception {
    mvc.perform(
            put("/api/cart/remove")
                .with(csrf())
                .content(objectMapper.writeValueAsString(invalidOrderItemRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @Test
  @DisplayName("valid 조건에 맞는 파라미터를 넘기면 장바구니 안의 특정 아이템을 수정에 성공한다. - DTO 검증")
  void update() throws Exception {
    mvc.perform(
            put("/api/cart/update")
                .with(csrf())
                .content(objectMapper.writeValueAsString(validOrderItemRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("invalid 조건에 맞는 파라미터를 넘기면 장바구니 안의 특정 아이템을 수정에 실패한다. - DTO 검증")
  void failUpdate() throws Exception {
    mvc.perform(
            put("/api/cart/update")
                .with(csrf())
                .content(objectMapper.writeValueAsString(invalidOrderItemRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }
}
