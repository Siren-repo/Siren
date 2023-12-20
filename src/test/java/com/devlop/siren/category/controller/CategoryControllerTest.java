package com.devlop.siren.category.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devlop.siren.domain.category.controller.CategoryController;
import com.devlop.siren.domain.category.dto.request.CategoryCreateRequest;
import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.category.service.CategoryService;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
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

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
  @Autowired private MockMvc mvc;

  @MockBean private CategoryService categoryService;

  @Autowired private ObjectMapper objectMapper;
  private CategoryCreateRequest validObject;
  private CategoryCreateRequest inValidObject;
  private UserDetailsDto userDetailsDto;
  private UserDetailsDto unAuthorizedUser;

  @BeforeEach
  private void setUp() {
    validObject = new CategoryCreateRequest(CategoryType.BEVERAGE, "에스프레소");
    inValidObject = new CategoryCreateRequest(CategoryType.BEVERAGE, " ");
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
  @DisplayName("Valid 조건에 맞는 파라미터를 넘기면 카테고리 생성에 성공한다 - DTO 검증")
  void createCategory() throws Exception {
    // given
    // when
    // then
    mvc.perform(
            post("/api/categories")
                .with(csrf())
                .content(objectMapper.writeValueAsString(validObject))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("Invalid 조건에 맞는 파라미터를 넘기면 카테고리 생성에 실패한다 - DTO 검증")
  void inValidCreateCategory() throws Exception {
    // given
    // when
    // then
    mvc.perform(
            post("/api/categories")
                .with(csrf())
                .content(objectMapper.writeValueAsString(inValidObject))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @Test
  @DisplayName("권한이 없는 경우 카테고리 생성에 실패한다 - DTO 검증")
  void failCreateCategory() throws Exception {
    // given
    // when
    // then
    registerUnAuthorizedUser();
    mvc.perform(
            post("/api/categories")
                .with(csrf())
                .content(objectMapper.writeValueAsString(validObject))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @DisplayName("Valid 조건에 맞는 파라미터를 넘기면 카테고리 조회에 성공한다 - DTO 검증")
  void findCategoriesByCategoryType() throws Exception {
    mvc.perform(
            get("/api/categories").param("categoryType", validObject.getCategoryType().getName()))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " "})
  @DisplayName("InValid 조건에 맞는 파라미터를 넘기면 카테고리 조회에 실패한다 - DTO 검증")
  void inValidFindCategoriesByCategoryType(String categoryType) throws Exception {
    mvc.perform(get("/api/categories").param("categoryType", categoryType))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }
}
