package com.devlop.siren.domain.category.controller;

import com.devlop.siren.domain.category.dto.request.CategoryCreateRequest;
import com.devlop.siren.domain.category.dto.response.CategoryResponse;
import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.category.service.CategoryService;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.util.Permission;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

  private final CategoryService categoryService;

  @Permission(role = {UserRole.ADMIN})
  @PostMapping
  public ApiResponse<CategoryResponse> createCategory(
      @RequestBody @Valid CategoryCreateRequest categoryCreateRequest) {
    return ApiResponse.ok(
        ResponseCode.Normal.CREATE, categoryService.register(categoryCreateRequest));
  }

  @GetMapping
  public ApiResponse<Page<CategoryResponse>> findCategoriesByCategoryType(
      @NotBlank @RequestParam("categoryType") String categoryType,
      @PageableDefault(sort = "categoryId", direction = Sort.Direction.DESC) Pageable pageable) {
    return ApiResponse.ok(
        ResponseCode.Normal.RETRIEVE,
        categoryService.findAllByType(CategoryType.of(categoryType), pageable));
  }
}
