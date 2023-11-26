package com.devlop.siren.item.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devlop.siren.domain.category.dto.request.CategoryCreateRequest;
import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.item.controller.ItemController;
import com.devlop.siren.domain.item.dto.request.DefaultOptionCreateRequest;
import com.devlop.siren.domain.item.dto.request.ItemCreateRequest;
import com.devlop.siren.domain.item.dto.request.NutritionCreateRequest;
import com.devlop.siren.domain.item.entity.option.SizeType;
import com.devlop.siren.domain.item.service.ItemService;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.util.UserInformation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

  @Autowired MockMvc mvc;

  @MockBean ItemService itemService;

  @Autowired private ObjectMapper objectMapper;
  static ItemCreateRequest validObject;
  static ItemCreateRequest inValidObject;
  private static MockedStatic<UserInformation> userInformationMock;

  @BeforeAll
  private static void setUp() {
    validObject =
        new ItemCreateRequest(
            new CategoryCreateRequest(CategoryType.of("음료"), "에스프레소"),
            "아메리카노",
            5000,
            "아메리카노입니다",
            null,
            false,
            true,
            new DefaultOptionCreateRequest(2, 0, 0, 0, SizeType.of("Tall")),
            "우유, 대두",
            new NutritionCreateRequest(0, 2, 3, 0, 1, 2, 2, 0, 0, 0));
    inValidObject =
        new ItemCreateRequest(
            new CategoryCreateRequest(CategoryType.of("음료"), "에스프레소"),
            "아메리카노",
            -5,
            "아메리카노입니다",
            null,
            false,
            true,
            new DefaultOptionCreateRequest(2, 0, 0, 0, SizeType.of("Tall")),
            "우유, 대두",
            new NutritionCreateRequest(0, 2, 3, 0, 1, 2, 2, 0, 0, 0));
    userInformationMock = mockStatic(UserInformation.class);
  }

  @AfterAll
  private static void cleanUp() {
    userInformationMock.close();
  }

  @Test
  @DisplayName("Valid 조건에 맞는 파라미터를 넘기면 아이템 생성에 성공한다 - DTO 검증")
  @WithMockUser
  void createItem() throws Exception {
    // given
    // when
    when(UserInformation.validAdmin(any(UserDetailsDto.class))).thenReturn(true);
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
  @WithMockUser
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
  @WithMockUser
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
  @WithMockUser
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
  @WithMockUser
  void findItemDetail(Long itemId) throws Exception {
    mvc.perform(get("/api/items/{itemId}", itemId)).andExpect(status().isOk()).andDo(print());
  }

  @ParameterizedTest
  @ValueSource(longs = {-1L, 0L})
  @DisplayName("Invalid 조건에 맞는 파라미터를 넘기면 아이템 상세 조회에 실패한다 - DTO 검증")
  @WithMockUser
  void inValidFindItemDetail(Long itemId) throws Exception {
    mvc.perform(get("/api/items/{itemId}", itemId))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @ParameterizedTest
  @ValueSource(longs = {1L, 2L})
  @DisplayName("Valid 조건에 맞는 파라미터를 넘기면 아이템 삭제에 성공한다 - DTO 검증")
  @WithMockUser
  void deleteItem(Long itemId) throws Exception {
    when(UserInformation.validAdmin(any(UserDetailsDto.class))).thenReturn(true);
    mvc.perform(delete("/api/items/{itemId}", itemId).with(csrf()))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @ParameterizedTest
  @ValueSource(longs = {-1L, 0L})
  @DisplayName("Invalid 조건에 맞는 파라미터를 넘기면 아이템 삭제에 실패한다 - DTO 검증")
  @WithMockUser
  void inValidDeleteItem(Long itemId) throws Exception {
    when(UserInformation.validAdmin(any(UserDetailsDto.class))).thenReturn(true);
    mvc.perform(delete("/api/items/{itemId}", itemId).with(csrf()))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @ParameterizedTest
  @ValueSource(longs = {1L, 2L})
  @DisplayName("Valid 조건에 맞는 파라미터를 넘기면 아이템 수정에 성공한다 - DTO 검증")
  @WithMockUser
  void updateItem(Long itemId) throws Exception {
    when(UserInformation.validAdmin(any(UserDetailsDto.class))).thenReturn(true);
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
  @WithMockUser
  void inValidUpdateItem(Long itemId) throws Exception {
    when(UserInformation.validAdmin(any(UserDetailsDto.class))).thenReturn(true);
    mvc.perform(
            (put("/api/items/{itemId}", itemId)
                .with(csrf())
                .content(objectMapper.writeValueAsString(inValidObject))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }
}
