package com.devlop.siren.stock.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devlop.siren.domain.stock.controller.StockController;
import com.devlop.siren.domain.stock.dto.request.StockCreateRequest;
import com.devlop.siren.domain.stock.service.StockService;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.fixture.UserFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(StockController.class)
class StockControllerTest {
  private static final Long STORE_ID = 1L;
  private static final Long ITEM_ID = 1L;
  @Autowired MockMvc mvc;

  @MockBean StockService stockService;

  @Autowired private ObjectMapper objectMapper;

  private StockCreateRequest validObject;
  private StockCreateRequest inValidObject;
  private UserDetailsDto userDetailsDto;
  private UserDetailsDto unAuthorizedUser;

  @BeforeEach
  public void setUp() {
    validObject = new StockCreateRequest(STORE_ID, ITEM_ID, 1);
    inValidObject = new StockCreateRequest(STORE_ID, ITEM_ID, -1);
    userDetailsDto = UserFixture.get(UserRole.ADMIN);
    unAuthorizedUser = UserFixture.get(UserRole.CUSTOMER);
    SecurityContextHolder.getContext()
        .setAuthentication(
            new UsernamePasswordAuthenticationToken(
                userDetailsDto, null, userDetailsDto.getAuthorities()));
  }

  private void registerUnAuthorizedUser() {
    SecurityContextHolder.getContext()
        .setAuthentication(
            new UsernamePasswordAuthenticationToken(
                unAuthorizedUser, null, unAuthorizedUser.getAuthorities()));
  }

  @Test
  @DisplayName("Valid 조건에 맞는 파라미터를 넘기면 재고 생성에 성공한다 - DTO 검증")
  void create() throws Exception {
    mvc.perform(
            post("/api/stocks")
                .with(csrf())
                .content(objectMapper.writeValueAsString(validObject))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("inValid 조건에 맞는 파라미터를 넘기면 재고 생성에 실패한다 - DTO 검증")
  void inValidCreate() throws Exception {
    mvc.perform(
            post("/api/stocks")
                .with(csrf())
                .content(objectMapper.writeValueAsString(inValidObject))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @Test
  @DisplayName("권한에 맞지 않으면 재고 생성에 실패한다 - DTO 검증")
  void failCreate() throws Exception {
    registerUnAuthorizedUser();
    mvc.perform(
            post("/api/stocks")
                .with(csrf())
                .content(objectMapper.writeValueAsString(inValidObject))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @DisplayName("Valid 조건에 맞는 파라미터를 넘기면 매장에 따른 재고 리스트 조회에 성공한다 - DTO 검증")
  void findAllByStore() throws Exception {
    mvc.perform(get("/api/stocks/{storeId}", STORE_ID)).andExpect(status().isOk()).andDo(print());
  }

  @Test
  @DisplayName("inValid 조건에 맞는 파라미터를 넘기면 매장에 따른 재고 리스트 조회에 실패한다 - DTO 검증")
  void inValidFindAllByStore() throws Exception {
    mvc.perform(get("/api/stocks/{storeId}", 0L)).andExpect(status().isBadRequest()).andDo(print());
  }

  @Test
  @DisplayName("권한에 맞지 않으면 매장에 따른 재고 리스트 조회에 실패한다 - DTO 검증")
  void failFindAllByStore() throws Exception {
    registerUnAuthorizedUser();
    mvc.perform(get("/api/stocks/{storeId}", STORE_ID))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @DisplayName("Valid 조건에 맞는 파라미터를 넘기면 매장, 아이템에 따른 재고 조회에 성공한다 - DTO 검증")
  void findStockDetail() throws Exception {
    mvc.perform(get("/api/stocks/{storeId}/{itemId}", STORE_ID, ITEM_ID))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("inValid 조건에 맞는 파라미터를 넘기면 매장, 아이템에 따른 재고 조회에 실패한다 - DTO 검증")
  void inValidFindStockDetail() throws Exception {
    mvc.perform(get("/api/stocks/{storeId}/{itemId}", 0L, 0L))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @Test
  @DisplayName("권한에 맞지 않으면 매장, 아이템에 따른 재고 조회에 실패한다 - DTO 검증")
  void failFindStockDetail() throws Exception {
    registerUnAuthorizedUser();
    mvc.perform(get("/api/stocks/{storeId}/{itemId}", STORE_ID, ITEM_ID))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @DisplayName("Valid 조건에 맞는 파라미터를 넘기면 재고 수정에 성공한다 - DTO 검증")
  void update() throws Exception {
    mvc.perform(
            put("/api/stocks/{storeId}/{itemId}", STORE_ID, ITEM_ID)
                .param("stock", validObject.getStock().toString())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("inValid 조건에 맞는 파라미터를 넘기면 재고 수정에 실패한다 - DTO 검증")
  void inValidUpdate() throws Exception {
    mvc.perform(
            put("/api/stocks/{storeId}/{itemId}", STORE_ID, ITEM_ID)
                .with(csrf())
                .content(objectMapper.writeValueAsString(inValidObject.getStock()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @Test
  @DisplayName("권한에 맞지 않으면 재고 수정에 실패한다 - DTO 검증")
  void failUpdate() throws Exception {
    registerUnAuthorizedUser();
    mvc.perform(
            put("/api/stocks/{storeId}/{itemId}", STORE_ID, ITEM_ID)
                .with(csrf())
                .content(objectMapper.writeValueAsString(validObject.getStock()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @DisplayName("Valid 조건에 맞는 파라미터를 넘기면 재고 삭제에 성공한다 - DTO 검증")
  void deleteStock() throws Exception {
    mvc.perform(
            delete("/api/stocks/{storeId}/{itemId}", STORE_ID, ITEM_ID)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("inValid 조건에 맞는 파라미터를 넘기면 재고 삭제에 실패한다 - DTO 검증")
  void inValidDeleteStock() throws Exception {
    mvc.perform(
            delete("/api/stocks/{storeId}/{itemId}", 0L, 0L)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @Test
  @DisplayName("권한이 맞지 않으면 재고 삭제에 실패한다 - DTO 검증")
  void failDeleteStock() throws Exception {
    registerUnAuthorizedUser();
    mvc.perform(
            delete("/api/stocks/{storeId}/{itemId}", STORE_ID, ITEM_ID)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andDo(print());
  }
}
