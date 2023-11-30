package com.devlop.siren.domain.cart.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devlop.siren.domain.cart.dto.ItemDto;
import com.devlop.siren.domain.cart.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CartController.class)
class CartControllerTest {
  @Autowired MockMvc mvc;

  @MockBean CartService cartService;

  @Autowired private ObjectMapper objectMapper;
  private final ItemDto validItemDto = new ItemDto(1L, 5L);
  private final ItemDto invalidItemDto = new ItemDto(null, null);

  @Test
  @DisplayName("valid 조건에 맞는 파라미터를 넘기면 장바구니 생성에 성공한다 - DTO 검증")
  @WithMockUser
  void add() throws Exception {
    mvc.perform(
            post("/api/cart")
                .with(csrf())
                .content(objectMapper.writeValueAsString(validItemDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("invalid 조건에 맞는 파라미터를 넘기면 장바구니 생성에 실패한다 - DTO 검증")
  @WithMockUser
  void failAdd() throws Exception {
    mvc.perform(
            post("/api/cart")
                .with(csrf())
                .content(objectMapper.writeValueAsString(invalidItemDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @Test
  @DisplayName("장바구니 조회에 성공한다 - DTO 검증")
  @WithMockUser
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
  @WithMockUser
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
  @WithMockUser
  void remove() throws Exception {
    mvc.perform(delete("/api/cart/{itemId}"
                    , validItemDto.getItemId())
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
  }

  @Test
  @DisplayName("invalid 조건에 맞는 파라미터를 넘기면 장바구니 안의 특정 아이템을 삭제에 실패한다. - DTO 검증")
  @WithMockUser
  void failRemove() throws Exception {
    mvc.perform(delete("/api/cart/{itemId}"
                    , 0L)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print());
  }

  @Test
  @DisplayName("valid 조건에 맞는 파라미터를 넘기면 장바구니 안의 특정 아이템을 수정에 성공한다. - DTO 검증")
  @WithMockUser
  void update() throws Exception {
    mvc.perform(put("/api/cart")
                    .with(csrf())
                    .content(objectMapper.writeValueAsString(validItemDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
  }
  @Test
  @DisplayName("invalid 조건에 맞는 파라미터를 넘기면 장바구니 안의 특정 아이템을 수정에 실패한다. - DTO 검증")
  @WithMockUser
  void failUpdate() throws Exception {
    mvc.perform(put("/api/cart")
                    .with(csrf())
                    .content(objectMapper.writeValueAsString(invalidItemDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print());
  }
}
