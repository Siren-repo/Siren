package com.devlop.siren.domain.category.service;

import com.devlop.siren.domain.category.dto.request.CategoryCreateRequest;
import com.devlop.siren.domain.category.dto.response.CategoryResponse;
import com.devlop.siren.domain.category.entity.Category;
import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.category.repository.CategoryRepository;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;

  @Transactional
  public CategoryResponse register(CategoryCreateRequest request) {
    validateDuplicateCategory(request.getCategoryType(), request.getCategoryName());
    Category category = CategoryCreateRequest.toEntity(request);
    return CategoryResponse.from(categoryRepository.save(category));
  }

  protected void validateDuplicateCategory(CategoryType categoryType, String categoryName) {
    categoryRepository
        .findByCategoryTypeAndCategoryName(categoryType, categoryName)
        .ifPresent(
            o -> {
              throw new GlobalException(ResponseCode.ErrorCode.DUPLICATE_CATEGORY);
            });
  }

  public Page<CategoryResponse> findAllByType(CategoryType categoryType, Pageable pageable) {
    PageRequest pageRequest =
        PageRequest.of(
            pageable.getPageNumber(), pageable.getPageSize(), Sort.by("categoryId").descending());
    Page<Category> categories =
        Optional.of(
                categoryRepository.findByCategoryTypeOrderByCategoryId(categoryType, pageRequest))
            .orElseThrow(() -> new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_CATEGORY));
    return categories.map(CategoryResponse::from);
  }
}
