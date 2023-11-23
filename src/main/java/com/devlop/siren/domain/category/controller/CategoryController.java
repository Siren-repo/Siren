package com.devlop.siren.domain.category.controller;

import com.devlop.siren.domain.category.dto.request.CategoryCreateRequest;
import com.devlop.siren.domain.category.dto.response.CategoryResponse;
import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.category.service.CategoryService;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

  private final CategoryService categoryService;

  @PostMapping
  public ApiResponse<CategoryResponse> createCategory(
      @RequestBody @Valid CategoryCreateRequest categoryCreateRequest,
      @AuthenticationPrincipal UserDetailsDto user) {
    return ApiResponse.ok(
        ResponseCode.Normal.CREATE, categoryService.register(categoryCreateRequest, user));
  }

  @GetMapping
  public ApiResponse<Page<CategoryResponse>> findCategoriesByCategoryType(
      @NotBlank @RequestParam("categoryType") String categoryType,
      @PageableDefault(size = 5, sort = "categoryId", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ApiResponse.ok(
        ResponseCode.Normal.RETRIEVE,
        categoryService.findAllByType(CategoryType.of(categoryType), pageable));
  }
}
