package com.devlop.siren.domain.category.dto.response;

import com.devlop.siren.domain.category.entity.Category;
import com.devlop.siren.domain.category.entity.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponse {

  private Long categoryId;

  private String categoryName;

  private CategoryType categoryType;

  public static CategoryResponse from(Category category) {
    return new CategoryResponse(
        category.getCategoryId(), category.getCategoryName(), category.getCategoryType());
  }
}
