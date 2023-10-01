package com.devlop.siren.domain.category.controller;


import com.devlop.siren.domain.category.dto.request.CategoryCreateRequest;
import com.devlop.siren.domain.category.dto.response.CategoriesResponse;
import com.devlop.siren.domain.category.dto.response.CategoryResponse;
import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.category.service.CategoryService;
import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 타입과 카테고리 이름으로 카테고리 생성
    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(@RequestBody @Valid CategoryCreateRequest categoryCreateRequest) {
        CategoryResponse categoryResponse = categoryService.register(categoryCreateRequest);
        return ApiResponse.createSuccess(ResponseCode.Normal.CREATE, categoryResponse);
    }

    // 카테고리 타입(음료) 별 카테고리 이름(에스프레소) 리스트 조회
    @GetMapping
    public ApiResponse<CategoriesResponse> findCategoriesByCategoryType(@NotBlank @RequestParam("categoryType") String categoryType) {
        CategoriesResponse categories = categoryService.findAllByType(CategoryType.of(categoryType));
        return ApiResponse.createSuccess(ResponseCode.Normal.RETRIEVE, categories);
    }

}
