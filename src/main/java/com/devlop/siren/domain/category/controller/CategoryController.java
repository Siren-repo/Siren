package com.devlop.siren.domain.category.controller;

import com.devlop.siren.domain.category.dto.request.CategoryCreateRequest;
import com.devlop.siren.domain.category.dto.response.CategoriesResponse;
import com.devlop.siren.domain.category.dto.response.CategoryResponse;
import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.category.service.CategoryService;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.util.UserInformation;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    UserInformation.validAdmin(user);
    CategoryResponse categoryResponse = categoryService.register(categoryCreateRequest);
    return ApiResponse.ok(ResponseCode.Normal.CREATE, categoryResponse);
  }

  @GetMapping
  public ApiResponse<CategoriesResponse> findCategoriesByCategoryType(
      @NotBlank @RequestParam("categoryType") String categoryType) {
    CategoriesResponse categories = categoryService.findAllByType(CategoryType.of(categoryType));
    return ApiResponse.ok(ResponseCode.Normal.RETRIEVE, categories);
  }
}
