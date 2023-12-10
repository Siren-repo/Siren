package com.devlop.siren.item.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devlop.siren.domain.category.dto.request.CategoryCreateRequest;
import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.item.controller.ItemController;
import com.devlop.siren.domain.item.dto.request.ItemCreateRequest;
import com.devlop.siren.domain.item.service.ItemService;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.fixture.ItemFixture;
import com.devlop.siren.fixture.UserFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

  @Autowired MockMvc mvc;

  @MockBean ItemService itemService;

  @Autowired private ObjectMapper objectMapper;
  private ItemCreateRequest validObject;
  private ItemCreateRequest inValidObject;
  private UserDetailsDto userDetailsDto;
  private UserDetailsDto unAuthorizedUser;

  @BeforeEach
  private void setUp() {
    validObject = ItemFixture.get(new CategoryCreateRequest(CategoryType.of("음료"), "에스프레소"), 5000);
    inValidObject = ItemFixture.get(new CategoryCreateRequest(CategoryType.of("음료"), "dd"), -5);
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
  @DisplayName("Valid 조건에 맞는 파라미터를 넘기면 아이템 생성에 성공한다 - DTO 검증")
  void createItem() throws Exception {
    // given
    // when
    // then
    mvc.perform(
            post("/api/items")
                .with(csrf())
                .content(objectMapper.writeValueAsString(validObject))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("InValid 조건에 맞는 파라미터를 넘기면 아이템 생성에 실패한다 - DTO 검증")
  void inValidCreateItem() throws Exception {
    // given
    // when
    // then
    mvc.perform(
            post("/api/items")
                .with(csrf())
                .content(objectMapper.writeValueAsString(inValidObject))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @Test
  @DisplayName("Valid 조건에 맞는 파라미터를 넘기면 아이템 리스트 조회에 성공한다 - DTO 검증")
  void findAllByCategory() throws Exception {
    mvc.perform(
            get("/api/items")
                .param("categoryType", validObject.getCategoryRequest().getCategoryType().getName())
                .param("categoryName", validObject.getCategoryRequest().getCategoryName()))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " "})
  @DisplayName("InValid 조건에 맞는 파라미터를 넘기면 아이템 리스트 조회에 실패한다 - DTO 검증")
  void inValidFindAllByCategory(String categoryType) throws Exception {
    mvc.perform(
            get("/api/items")
                .param("categoryType", categoryType)
                .param("categoryName", inValidObject.getCategoryRequest().getCategoryName()))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @ParameterizedTest
  @ValueSource(longs = {1L, 2L})
  @DisplayName("Valid 조건에 맞는 파라미터를 넘기면 아이템 상세 조회에 성공한다 - DTO 검증")
  void findItemDetail(Long itemId) throws Exception {
    mvc.perform(get("/api/items/{itemId}", itemId)).andExpect(status().isOk()).andDo(print());
  }

  @ParameterizedTest
  @ValueSource(longs = {-1L, 0L})
  @DisplayName("Invalid 조건에 맞는 파라미터를 넘기면 아이템 상세 조회에 실패한다 - DTO 검증")
  void inValidFindItemDetail(Long itemId) throws Exception {
    mvc.perform(get("/api/items/{itemId}", itemId))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @ParameterizedTest
  @ValueSource(longs = {1L, 2L})
  @DisplayName("Valid 조건에 맞는 파라미터를 넘기면 아이템 삭제에 성공한다 - DTO 검증")
  void deleteItem(Long itemId) throws Exception {
    mvc.perform(delete("/api/items/{itemId}", itemId).with(csrf()))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @ParameterizedTest
  @ValueSource(longs = {-1L, 0L})
  @DisplayName("Invalid 조건에 맞는 파라미터를 넘기면 아이템 삭제에 실패한다 - DTO 검증")
  void inValidDeleteItem(Long itemId) throws Exception {
    mvc.perform(delete("/api/items/{itemId}", itemId).with(csrf()))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @ParameterizedTest
  @ValueSource(longs = {1L, 2L})
  @DisplayName("Valid 조건에 맞는 파라미터를 넘기면 아이템 수정에 성공한다 - DTO 검증")
  void updateItem(Long itemId) throws Exception {
    mvc.perform(
            put("/api/items/{itemId}", itemId)
                .with(csrf())
                .content(objectMapper.writeValueAsString(validObject))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @ParameterizedTest
  @ValueSource(longs = {-1L, 0L})
  @DisplayName("Invalid 조건에 맞는 파라미터를 넘기면 아이템 수정에 실패한다 - DTO 검증")
  void inValidUpdateItem(Long itemId) throws Exception {
    mvc.perform(
            (put("/api/items/{itemId}", itemId)
                .with(csrf())
                .content(objectMapper.writeValueAsString(inValidObject))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @Test
  @DisplayName("권한이 없는 경우 아이템 생성에 실패한다")
  void failCreateItem() throws Exception {
    // given
    // when
    // then
    registerUnAuthorizedUser();
    mvc.perform(
            post("/api/items")
                .with(csrf())
                .content(objectMapper.writeValueAsString(validObject))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @DisplayName("권한이 없는 경우 아이템 수정에 실패한다")
  void failUpdateItem() throws Exception {
    registerUnAuthorizedUser();
    mvc.perform(
            put("/api/items/{itemId}", 1L)
                .with(csrf())
                .content(objectMapper.writeValueAsString(validObject))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @DisplayName("권한이 없는 경우 아이템 삭제에 실패한다")
  void failDeleteItem() throws Exception {
    registerUnAuthorizedUser();
    mvc.perform(delete("/api/items/{itemId}", 1L).with(csrf()))
        .andExpect(status().isForbidden())
        .andDo(print());
  }
}
