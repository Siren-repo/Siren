package com.devlop.siren.category.controller;

import com.devlop.siren.domain.category.controller.CategoryController;
import com.devlop.siren.domain.category.dto.request.CategoryCreateRequest;
import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.category.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CategoryController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CategoryControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;
    static CategoryCreateRequest validObject;
    static CategoryCreateRequest inValidObject;

    @BeforeAll
    private static void setUp() {
        validObject = new CategoryCreateRequest(CategoryType.BEVERAGE, "에스프레소");
        inValidObject = new CategoryCreateRequest(CategoryType.BEVERAGE, " ");
    }

    @Test
    @DisplayName("Valid 조건에 맞는 파라미터를 넘기면 카테고리 생성에 성공한다 - DTO 검증")
    void createCategory() throws Exception {
        //given
        //when
        //then
        mvc.perform(post("/api/categories")
                        .content(objectMapper.writeValueAsString(validObject))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("Invalid 조건에 맞는 파라미터를 넘기면 카테고리 생성에 실패한다 - DTO 검증")
    void inValidCreateCategory() throws Exception {
        //given
        //when
        //then
        mvc.perform(post("/api/categories")
                        .content(objectMapper.writeValueAsString(inValidObject))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andDo(print());

    }

    @Test
    @DisplayName("Valid 조건에 맞는 파라미터를 넘기면 카테고리 조회에 성공한다 - DTO 검증")
    void findCategoriesByCategoryType() throws Exception {
        mvc.perform(get("/api/categories")
                        .param("categoryType", validObject.getCategoryType().getName()))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("InValid 조건에 맞는 파라미터를 넘기면 카테고리 조회에 실패한다 - DTO 검증")
    void inValidFindCategoriesByCategoryType(String categoryType) throws Exception {
        mvc.perform(get("/api/categories")
                        .param("categoryType", categoryType))
                .andExpect(status().isNotAcceptable())
                .andDo(print());

    }
}